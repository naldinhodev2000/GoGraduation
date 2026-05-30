package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.GroupUserDTO;
import fafenterprise.dev.gograduation.entity.relationship.GroupUserEntity;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.enums.GroupUserStatus;
import fafenterprise.dev.gograduation.enums.Role;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import fafenterprise.dev.gograduation.repository.GroupUserRepository;
import fafenterprise.dev.gograduation.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupUserService {
    final GroupUserRepository groupUserRepo;
    final UserRepository userRepo;
    final GroupRepository groupRepo;

    public void create(UUID createdBy, UUID groupId) {

        GroupEntity group = groupRepo.findById(groupId).orElseThrow();
        UserEntity user = userRepo.findById(createdBy).orElseThrow();

        GroupUserEntity groupUser = new GroupUserEntity();
        groupUser.setUser(user);
        groupUser.setGroup(group);
        groupUser.setJoinedAt(LocalDateTime.now());
        groupUser.setRole(Role.ADMIN);
        groupUser.setStatus(GroupUserStatus.ACTIVE);

        groupUserRepo.save(groupUser);
    }

    public void addUser(GroupUserDTO groupUserDTO) {
        GroupEntity group = groupRepo.findById(groupUserDTO.idGroup()).orElseThrow();
        UserEntity user = userRepo.findById(groupUserDTO.idUser()).orElseThrow();

        GroupUserEntity groupUser = new GroupUserEntity();
        groupUser.setUser(user);
        groupUser.setGroup(group);
        groupUser.setJoinedAt(LocalDateTime.now());
        groupUser.setRole(Role.MEMBER);
        groupUser.setStatus(GroupUserStatus.ACTIVE);

        groupUserRepo.save(groupUser);

    }
}
