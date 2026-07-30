package fafenterprise.dev.gograduation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fafenterprise.dev.gograduation.dto.request.UserRequestDTO;
import fafenterprise.dev.gograduation.dto.response.GroupResponseDTO;
import fafenterprise.dev.gograduation.dto.response.UserResponseDTO;
import fafenterprise.dev.gograduation.services.GroupUserService;
import fafenterprise.dev.gograduation.services.UserService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final GroupUserService groupUserService;

    @GetMapping
    public List<UserResponseDTO> listAll() {
        return userService.listAll();
    }

    @PutMapping("/{id}")
    public UserResponseDTO update(@PathVariable UUID id, @RequestBody UserRequestDTO usuario) {
        return userService.update(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @GetMapping("/me/groups")
    public List<GroupResponseDTO> getJoinedGroups() {
        return groupUserService.getJoinedGroups();
    }
    
}
