
package fafenterprise.dev.gograduation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fafenterprise.dev.gograduation.dto.GroupUserDTO;
import fafenterprise.dev.gograduation.dto.request.GroupRequestDTO;
import fafenterprise.dev.gograduation.dto.request.JoinGroupDTO;
import fafenterprise.dev.gograduation.dto.response.GroupResponseDTO;
import fafenterprise.dev.gograduation.dto.response.MemberSummaryDTO;
import fafenterprise.dev.gograduation.dto.response.UserResponseDTO;
import fafenterprise.dev.gograduation.services.GroupService;
import fafenterprise.dev.gograduation.services.GroupUserService;
import fafenterprise.dev.gograduation.services.MemberSummaryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupUserService groupUserService;
    private final MemberSummaryService memberSummaryService;

    /*
     * Cria uma nova sala.
     *
     * O usuário logado será automaticamente
     * definido como ADMIN.
     */
    @PostMapping
    public GroupResponseDTO create(
            @RequestBody GroupRequestDTO groupRequestDTO) {

        return groupService.create(groupRequestDTO);
    }

    /*
     * Lista somente as salas
     * em que o usuário logado participa.
     */
    @GetMapping
    public List<GroupResponseDTO> listMyGroups() {

        return groupUserService.getJoinedGroups();
    }

    /*
     * Busca uma sala específica.
     *
     * Somente membros da sala podem visualizar.
     */
    @GetMapping("/{id}")
    public GroupResponseDTO findById(
            @PathVariable UUID id) {

        groupUserService.validateUserInGroup(id);

        return groupService.findById(id);
    }

    /*
     * Atualiza uma sala.
     *
     * Somente ADMIN pode alterar.
     */
    @PutMapping("/{id}")
    public GroupResponseDTO update(
            @PathVariable UUID id,
            @RequestBody GroupRequestDTO group) {

        groupUserService.validateAdmin(id);

        return groupService.update(
                id,
                group
        );
    }

    /*
     * Exclui uma sala.
     *
     * Somente ADMIN pode excluir.
     */
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id) {

        groupUserService.validateAdmin(id);

        groupService.delete(id);
    }

    /*
     * Adiciona um usuário à sala.
     *
     * Somente ADMIN pode adicionar.
     */
    @PostMapping("/{groupId}/users")
    public void addUser(
            @PathVariable UUID groupId,
            @RequestBody GroupUserDTO dto) {

        groupUserService.validateAdmin(groupId);

        GroupUserDTO groupUserDTO =
                new GroupUserDTO(
                        groupId,
                        dto.idUser()
                );

        groupUserService.addUser(
                groupUserDTO
        );
    }

    /*
     * Remove um usuário da sala.
     *
     * O próprio GroupUserService
     * verifica se quem está removendo é ADMIN.
     */
    @DeleteMapping("/{groupId}/users/{userId}")
    public void removeUser(
            @PathVariable UUID groupId,
            @PathVariable UUID userId) {

        groupUserService.removeUser(
                groupId,
                userId
        );
    }

    /*
     * Entra em uma sala através do token.
     *
     * O usuário precisa estar autenticado.
     */
    @PostMapping("/join")
    public void joinGroup(
            @RequestBody JoinGroupDTO dto) {

        groupUserService.joinGroup(
                dto.token()
        );
    }

    /*
     * Lista os membros de uma sala.
     *
     * Somente membros da sala podem visualizar.
     */
    @GetMapping("/{groupId}/members")
    public List<UserResponseDTO> getClassmates(
            @PathVariable UUID groupId) {

        groupUserService.validateUserInGroup(
                groupId
        );

        return groupUserService.getClassemates(
                groupId
        );
    }

    /*
     * Busca o resumo financeiro de um membro.
     *
     * Somente membros da sala podem visualizar.
     */
    @GetMapping("/{groupId}/members/{userId}/summary")
    public MemberSummaryDTO getMemberSummary(
            @PathVariable UUID groupId,
            @PathVariable UUID userId) {

        groupUserService.validateUserInGroup(
                groupId
        );

        return memberSummaryService.getSummary(
                groupId,
                userId
        );
    }
}
