package fafenterprise.dev.gograduation.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.request.RaffleSellerRequestDTO;
import fafenterprise.dev.gograduation.dto.response.RaffleSellerResponseDTO;
import fafenterprise.dev.gograduation.entity.relationship.RaffleSellerEntity;
import fafenterprise.dev.gograduation.entity.uno.RaffleEntity;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.repository.RaffleRepository;
import fafenterprise.dev.gograduation.repository.RaffleSellerRepository;
import fafenterprise.dev.gograduation.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RaffleSellerService {

    private final RaffleSellerRepository raffleSellerRepository;
    private final RaffleRepository raffleRepository;
    private final UserRepository userRepository;

    public RaffleSellerResponseDTO create(
            RaffleSellerRequestDTO request) {

        RaffleEntity raffle = raffleRepository
                .findById(request.raffleId())
                .orElseThrow(() ->
                        new RuntimeException("Raffle not found"));

        UserEntity user = userRepository
                .findById(request.userId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        BigDecimal amountDue = calculateAmountDue(
                raffle,
                request.quantity()
        );

        RaffleSellerEntity entity = new RaffleSellerEntity();

        entity.setRaffle(raffle);
        entity.setUser(user);
        entity.setQuantity(request.quantity());
        entity.setAmountDue(amountDue);

        RaffleSellerEntity saved =
                raffleSellerRepository.save(entity);

        return toResponse(saved);
    }

    public RaffleSellerResponseDTO findById(UUID id) {

        RaffleSellerEntity entity =
                raffleSellerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Raffle seller not found"));

        return toResponse(entity);
    }

    public List<RaffleSellerResponseDTO> findByUser(
            UUID userId) {

        return raffleSellerRepository
                .findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RaffleSellerResponseDTO> findByUserAndGroup(
            UUID groupId,
            UUID userId) {

        return raffleSellerRepository
                .findByUserIdAndRaffleGroupId(
                        userId,
                        groupId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RaffleSellerResponseDTO update(
            UUID id,
            RaffleSellerRequestDTO request) {

        RaffleSellerEntity entity =
                raffleSellerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Raffle seller not found"));

        RaffleEntity raffle = raffleRepository
                .findById(request.raffleId())
                .orElseThrow(() ->
                        new RuntimeException("Raffle not found"));

        UserEntity user = userRepository
                .findById(request.userId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        BigDecimal amountDue = calculateAmountDue(
                raffle,
                request.quantity()
        );

        entity.setRaffle(raffle);
        entity.setUser(user);
        entity.setQuantity(request.quantity());
        entity.setAmountDue(amountDue);

        RaffleSellerEntity updated =
                raffleSellerRepository.save(entity);

        return toResponse(updated);
    }

    public void delete(UUID id) {

        RaffleSellerEntity entity =
                raffleSellerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Raffle seller not found"));

        raffleSellerRepository.delete(entity);
    }

    private BigDecimal calculateAmountDue(
            RaffleEntity raffle,
            Integer quantity) {

        if (quantity == null || quantity < 0) {
            throw new RuntimeException(
                    "Quantity must be greater than or equal to zero"
            );
        }

        if (raffle.getValue() == null) {
            throw new RuntimeException(
                    "Raffle value cannot be null"
            );
        }

        return raffle.getValue()
                .multiply(BigDecimal.valueOf(quantity));
    }

    private RaffleSellerResponseDTO toResponse(
            RaffleSellerEntity entity) {

        return new RaffleSellerResponseDTO(
                entity.getId(),
                entity.getRaffle().getId(),
                entity.getUser().getId(),
                entity.getQuantity(),
                entity.getAmountDue()
        );
    }
}