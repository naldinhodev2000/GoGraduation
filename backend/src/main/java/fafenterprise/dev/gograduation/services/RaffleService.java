
package fafenterprise.dev.gograduation.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fafenterprise.dev.gograduation.dto.request.RaffleRequestDTO;
import fafenterprise.dev.gograduation.dto.response.RaffleResponseDTO;
import fafenterprise.dev.gograduation.entity.relationship.RaffleSellerEntity;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.entity.uno.RaffleEntity;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import fafenterprise.dev.gograduation.repository.RaffleRepository;
import fafenterprise.dev.gograduation.repository.RaffleSellerRepository;
import fafenterprise.dev.gograduation.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RaffleService {

    private final RaffleRepository raffleRepo;
    private final GroupRepository groupRepo;
    private final RaffleSellerRepository raffleSellerRepository;
    private final UserRepository userRepo;
    private final GroupUserService groupUserService;

    /*
     * Cria uma nova rifa.
     *
     * Somente ADMIN da sala pode criar.
     */
    public RaffleResponseDTO create(
            RaffleRequestDTO raffleRequestDTO) {

        UUID groupId =
                raffleRequestDTO.groupId();

        validateAdmin(groupId);

        GroupEntity group =
                groupRepo
                        .findById(groupId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Group not found"));

        RaffleEntity raffle =
                new RaffleEntity();

        raffle.setName(
                raffleRequestDTO.name());

        raffle.setGroup(group);

        raffle.setValue(
                raffleRequestDTO.value());

        raffle.setTotal(null);

        raffleRepo.save(raffle);

        return toResponseDTO(raffle);
    }

    /*
     * Lista as rifas de uma sala.
     *
     * Qualquer membro ativo pode visualizar.
     */
    public List<RaffleResponseDTO> listByGroup(
            UUID groupId) {

        validateMember(groupId);

        return raffleRepo
                .findByGroup_Id(groupId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /*
     * Atualiza uma rifa.
     *
     * Somente ADMIN pode alterar.
     */
    public RaffleResponseDTO update(
            UUID id,
            RaffleRequestDTO raffleRequestDTO) {

        RaffleEntity raffle =
                raffleRepo
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Raffle not found"));

        UUID oldGroupId =
                raffle
                        .getGroup()
                        .getId();

        UUID newGroupId =
                raffleRequestDTO
                        .groupId();

        // Admin da sala atual
        validateAdmin(oldGroupId);

        // Se a rifa for movida para outra sala,
        // o usuário também precisa ser admin
        // da nova sala.
        if (!oldGroupId.equals(newGroupId)) {
            validateAdmin(newGroupId);
        }

        GroupEntity newGroup =
                groupRepo
                        .findById(newGroupId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Group not found"));

        raffle.setName(
                raffleRequestDTO.name());

        raffle.setTotal(
                raffleRequestDTO.total());

        raffle.setValue(
                raffleRequestDTO.value());

        raffle.setGroup(newGroup);

        raffleRepo.save(raffle);

        return toResponseDTO(raffle);
    }

    /*
     * Exclui uma rifa.
     *
     * Somente ADMIN da sala pode excluir.
     */
    public void delete(UUID id) {

        RaffleEntity raffle =
                raffleRepo
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Raffle not found"));

        UUID groupId =
                raffle
                        .getGroup()
                        .getId();

        validateAdmin(groupId);

        raffleRepo.delete(raffle);
    }

    /*
     * Define um vendedor para uma rifa.
     *
     * O vendedor precisa pertencer
     * à mesma sala da rifa.
     */
    public void setRaffleSeller(
            UUID raffleId,
            UUID userId) {

        RaffleEntity raffle =
                raffleRepo
                        .findById(raffleId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Raffle not found"));

        UUID groupId =
                raffle
                        .getGroup()
                        .getId();

        // Somente ADMIN pode definir vendedores
        validateAdmin(groupId);

        UserEntity seller =
                userRepo
                        .findById(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        // O vendedor precisa pertencer
        // à mesma sala da rifa
        if (!groupUserService.isUserInGroup(
                groupId,
                userId)) {

            throw new RuntimeException(
                    "Seller is not a member of this group");
        }

        RaffleSellerEntity raffleSeller =
                new RaffleSellerEntity();

        raffleSeller.setRaffle(raffle);
        raffleSeller.setUser(seller);

        raffleSellerRepository.save(
                raffleSeller);
    }

    /*
     * Valida se o usuário logado é membro
     * ativo da sala.
     */
    private void validateMember(
            UUID groupId) {

        if (!groupUserService.isUserInGroup(
                groupId)) {

            throw new RuntimeException(
                    "User is not a member of the group");
        }
    }

    /*
     * Valida se o usuário logado é ADMIN
     * da sala.
     */
    private void validateAdmin(
            UUID groupId) {

        validateMember(groupId);

        if (!groupUserService.isUserAdmin(
                groupId)) {

            throw new RuntimeException(
                    "Only admins can perform this action");
        }
    }

    /*
     * Converte Entity para ResponseDTO.
     */
    private RaffleResponseDTO toResponseDTO(
            RaffleEntity raffle) {

        return new RaffleResponseDTO(
                raffle.getId(),
                raffle.getName(),
                raffle.getGroup().getId(),
                raffle.getValue(),
                raffle.getTotal()
        );
    }
}