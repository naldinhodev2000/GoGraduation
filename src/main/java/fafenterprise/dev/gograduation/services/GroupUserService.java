package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class GroupUserService {

    private final GroupUserRepository groupUserRepo;
    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final JwtService jwtService;

    public void create(UUID groupId) {

        UUID loggedUserId = jwtService.getLoggedId();

        GroupEntity group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        UserEntity user = userRepo.findById(loggedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupUserEntity groupUser = new GroupUserEntity();
        groupUser.setUser(user);
        groupUser.setGroup(group);
        groupUser.setJoinedAt(LocalDateTime.now());
        groupUser.setRole(Role.ADMIN);
        groupUser.setStatus(GroupUserStatus.ACTIVE);

        groupUserRepo.save(groupUser);
    }

    public void addUser(GroupUserDTO groupUserDTO) {

        GroupEntity group = groupRepo.findById(groupUserDTO.idGroup())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        UserEntity user = userRepo.findById(groupUserDTO.idUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupUserEntity existing = groupUserRepo
                .findByUser_IdAndGroup_Id(user.getId(), group.getId())
                .orElse(null);

        if (existing != null) {
            throw new RuntimeException("User already in group");
        }

        GroupUserEntity groupUser = new GroupUserEntity();
        groupUser.setUser(user);
        groupUser.setGroup(group);
        groupUser.setJoinedAt(LocalDateTime.now());
        groupUser.setRole(Role.MEMBER);
        groupUser.setStatus(GroupUserStatus.ACTIVE);

        groupUserRepo.save(groupUser);
    }

    public void removeUser(UUID groupId, UUID userIdToRemove) {

        UUID loggedUserId = jwtService.getLoggedId();

        GroupUserEntity adminMembership = groupUserRepo
                .findByUser_IdAndGroup_Id(loggedUserId, groupId)
                .orElseThrow(() -> new RuntimeException("User is not in this group"));

        if (adminMembership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admins can remove users from the group");
        }

        GroupUserEntity targetMembership = groupUserRepo
                .findByUser_IdAndGroup_Id(userIdToRemove, groupId)
                .orElseThrow(() -> new RuntimeException("Target user is not in this group"));

        if (targetMembership.getRole() == Role.ADMIN) {
            throw new RuntimeException("Cannot remove another admin");
        }

        targetMembership.setStatus(GroupUserStatus.REMOVED);

        groupUserRepo.save(targetMembership);
    }

    public void joinGroup(String groupToken) {

        
        UUID userId = jwtService.getLoggedId();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>" + userId);

        GroupEntity group = groupRepo.findByToken(groupToken)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupUserEntity existing = groupUserRepo
                .findByUser_IdAndGroup_Id(userId, group.getId())
                .orElse(null);

        if (existing == null) {

            GroupUserEntity groupUser = new GroupUserEntity();
            groupUser.setUser(user);
            groupUser.setGroup(group);
            groupUser.setJoinedAt(LocalDateTime.now());
            groupUser.setRole(Role.MEMBER);
            groupUser.setStatus(GroupUserStatus.ACTIVE);

            groupUserRepo.save(groupUser);
            return;
        }

        if (existing.getStatus() == GroupUserStatus.REMOVED) {
            existing.setStatus(GroupUserStatus.ACTIVE);
            groupUserRepo.save(existing);
            return;
        }

        throw new RuntimeException("User already in group");
    }
}