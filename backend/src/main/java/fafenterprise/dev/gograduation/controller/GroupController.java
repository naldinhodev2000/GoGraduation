
package fafenterprise.dev.gograduation.controller;

import fafenterprise.dev.gograduation.dto.GroupUserDTO;
import fafenterprise.dev.gograduation.dto.request.GroupRequestDTO;
import fafenterprise.dev.gograduation.dto.request.JoinGroupDTO;
import fafenterprise.dev.gograduation.dto.response.GroupResponseDTO;
import fafenterprise.dev.gograduation.dto.response.UserResponseDTO;
import fafenterprise.dev.gograduation.services.GroupService;
import fafenterprise.dev.gograduation.services.GroupUserService;
import lombok.RequiredArgsConstructor;

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
import fafenterprise.dev.gograduation.dto.response.MemberSummaryDTO;
import fafenterprise.dev.gograduation.services.MemberSummaryService;


@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupUserService groupUserService;
    private final MemberSummaryService memberSummaryService;

    @GetMapping("/{groupId}/members/{userId}/summary")
    public MemberSummaryDTO getMemberSummary(
            @PathVariable UUID groupId,
            @PathVariable UUID userId) {

        return memberSummaryService.getSummary(groupId, userId);
    }

    @PostMapping
    public GroupResponseDTO create(
            @RequestBody GroupRequestDTO groupRequestDTO) {

        return groupService.create(groupRequestDTO);
    }

    @GetMapping
    public List<GroupResponseDTO> listAll() {

        return groupService.listAll();
    }

    @GetMapping("/{id}")
    public GroupResponseDTO findById(
            @PathVariable UUID id) {

        return groupService.findById(id);
    }

    @PutMapping("/{id}")
    public GroupResponseDTO update(
            @PathVariable UUID id,
            @RequestBody GroupRequestDTO group) {

        return groupService.update(id, group);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id) {

        groupService.delete(id);
    }

    @PostMapping("/{groupId}/users")
    public void addUser(
            @PathVariable UUID groupId,
            @RequestBody GroupUserDTO dto) {

        GroupUserDTO groupUserDTO = new GroupUserDTO(groupId, dto.idUser());

        groupUserService.addUser(groupUserDTO);
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public void removeUser(
            @PathVariable UUID groupId,
            @PathVariable UUID userId) {

        groupUserService.removeUser(groupId, userId);
    }

    @PostMapping("/join")
    public void joinGroup(
            @RequestBody JoinGroupDTO dto) {

        groupUserService.joinGroup(dto.token());
    }

    @GetMapping("/{groupId}/members")
    public List<UserResponseDTO> getClassmates(
            @PathVariable UUID groupId) {

        return groupUserService.getClassemates(groupId);
    }
}
