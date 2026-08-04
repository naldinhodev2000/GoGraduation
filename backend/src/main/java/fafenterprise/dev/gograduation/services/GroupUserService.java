package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fafenterprise.dev.gograduation.dto.GroupUserDTO;
import fafenterprise.dev.gograduation.dto.response.GroupResponseDTO;
import fafenterprise.dev.gograduation.dto.response.UserResponseDTO;
import fafenterprise.dev.gograduation.dto.response.DetailedMemberDTO;
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

        GroupEntity group = groupRepo
                .findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        UserEntity user = userRepo
                .findById(loggedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupUserEntity groupUser = new GroupUserEntity();

        groupUser.setUser(user);
        groupUser.setGroup(group);
        groupUser.setJoinedAt(LocalDateTime.now());
        groupUser.setRole(Role.ADMIN);
        groupUser.setStatus(GroupUserStatus.ACTIVE);

        groupUserRepo.save(groupUser);
    }

    public void addUser(
            GroupUserDTO groupUserDTO) {

        UUID groupId = groupUserDTO.idGroup();

        if (!isUserInGroup(groupId)) {
            throw new RuntimeException(
                    "User is not a member of the group");
        }

        if (!isUserAdmin(groupId)) {
            throw new RuntimeException(
                    "Only admins can add users");
        }

        GroupEntity group = groupRepo
                .findById(groupId)
                .orElseThrow(() -> new RuntimeException(
                        "Group not found"));

        UserEntity user = userRepo
                .findById(groupUserDTO.idUser())
                .orElseThrow(() -> new RuntimeException(
                        "User not found"));

        GroupUserEntity existing = groupUserRepo
                .findByUser_IdAndGroup_Id(
                        user.getId(),
                        group.getId())
                .orElse(null);

        if (existing != null &&
                existing.getStatus() == GroupUserStatus.ACTIVE) {

            throw new RuntimeException(
                    "User already in group");
        }

        if (existing != null &&
                existing.getStatus() == GroupUserStatus.REMOVED) {

            existing.setStatus(
                    GroupUserStatus.ACTIVE);

            existing.setJoinedAt(
                    LocalDateTime.now());

            existing.setRole(Role.MEMBER);

            groupUserRepo.save(existing);
            return;
        }

        GroupUserEntity groupUser = new GroupUserEntity();

        groupUser.setUser(user);
        groupUser.setGroup(group);
        groupUser.setJoinedAt(LocalDateTime.now());
        groupUser.setRole(Role.MEMBER);
        groupUser.setStatus(GroupUserStatus.ACTIVE);

        groupUserRepo.save(groupUser);
    }

    public void removeUser(
            UUID groupId,
            UUID userIdToRemove) {

        UUID loggedUserId = jwtService.getLoggedId();

        GroupUserEntity adminMembership = groupUserRepo
                .findByUser_IdAndGroup_Id(
                        loggedUserId,
                        groupId)
                .orElseThrow(() -> new RuntimeException(
                        "User is not in this group"));

        if (adminMembership.getStatus() != GroupUserStatus.ACTIVE) {
            throw new RuntimeException("User is not active in this group");
        }

        if (adminMembership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admins can remove users from the group");
        }

        GroupUserEntity targetMembership = groupUserRepo
                .findByUser_IdAndGroup_Id(
                        userIdToRemove,
                        groupId)
                .orElseThrow(() -> new RuntimeException(
                        "Target user is not in this group"));

        if (targetMembership.getStatus() != GroupUserStatus.ACTIVE) {
            throw new RuntimeException("Target user is not active in this group");
        }

        if (targetMembership.getRole() == Role.ADMIN) {

            if (!loggedUserId.equals(userIdToRemove)) {
                throw new RuntimeException("Você não pode remover outro administrador.");
            }

            long adminCount = groupUserRepo.findByGroup_Id(groupId).stream()
                    .filter(u -> u.getStatus() == GroupUserStatus.ACTIVE && u.getRole() == Role.ADMIN)
                    .count();

            if (adminCount <= 1) {
                throw new RuntimeException("Operação negada: Você é o único administrador e não pode sair da sala. Promova outro membro a administrador primeiro.");
            }
        }

        targetMembership.setStatus(GroupUserStatus.REMOVED);
        groupUserRepo.save(targetMembership);
    }

    public void joinGroup(
            String groupToken) {

        UUID userId = jwtService.getLoggedId();

        GroupEntity group = groupRepo
                .findByToken(groupToken)
                .orElseThrow(() -> new RuntimeException(
                        "Group not found"));

        UserEntity user = userRepo
                .findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found"));

        GroupUserEntity existing = groupUserRepo
                .findByUser_IdAndGroup_Id(
                        userId,
                        group.getId())
                .orElse(null);

        if (existing == null) {

            GroupUserEntity groupUser = new GroupUserEntity();

            groupUser.setUser(user);
            groupUser.setGroup(group);
            groupUser.setJoinedAt(
                    LocalDateTime.now());
            groupUser.setRole(Role.MEMBER);
            groupUser.setStatus(
                    GroupUserStatus.ACTIVE);

            groupUserRepo.save(groupUser);
            return;
        }

        if (existing.getStatus() == GroupUserStatus.REMOVED) {

            existing.setStatus(
                    GroupUserStatus.ACTIVE);

            existing.setJoinedAt(
                    LocalDateTime.now());

            groupUserRepo.save(existing);
            return;
        }

        throw new RuntimeException(
                "User already in group");
    }

    public List<GroupResponseDTO> getJoinedGroups() {

        UUID userId = jwtService.getLoggedId();

        List<GroupUserEntity> groups = groupUserRepo
                .findByUserIdAndStatus(
                        userId,
                        GroupUserStatus.ACTIVE);

        return groups
                .stream()
                .map(groupUser -> {

                    GroupEntity group = groupUser.getGroup();

                    return new GroupResponseDTO(
                            group.getId(),
                            group.getName(),
                            group.getGoal(),
                            group.getTeam(),
                            group.getToken());
                })
                .toList();
    }

    public List<UserResponseDTO> getClassemates(
            UUID groupId) {

        if (!isUserInGroup(groupId)) {
            throw new RuntimeException(
                    "User is not a member of the group");
        }

        List<GroupUserEntity> groupUsers = groupUserRepo
                .findByGroup_Id(groupId);

        return groupUsers
                .stream()
                .filter(groupUser -> groupUser.getStatus() == GroupUserStatus.ACTIVE)
                .map(groupUser -> {

                    UserEntity user = groupUser.getUser();

                    return new UserResponseDTO(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getTelefone());
                })
                .toList();
    }

    public List<DetailedMemberDTO> getDetailedClassmates(UUID groupId) {

        validateUserInGroup(groupId);

        List<GroupUserEntity> groupUsers = groupUserRepo
                .findByGroup_Id(groupId);

        return groupUsers
                .stream()
                .filter(groupUser -> groupUser.getStatus() == GroupUserStatus.ACTIVE)
                .map(groupUser -> {

                    UserEntity user = groupUser.getUser();

                    return new DetailedMemberDTO(
                            user.getId(),
                            user.getName(),
                            groupUser.getRole().name()
                    );
                })
                .toList();
    }

    public boolean isUserInGroup(
            UUID groupId) {

        UUID userId = jwtService.getLoggedId();

        return isUserInGroup(
                groupId,
                userId);
    }

    public boolean isUserInGroup(
            UUID groupId,
            UUID userId) {

        return groupUserRepo
                .existsByUser_IdAndGroup_IdAndStatus(
                        userId,
                        groupId,
                        GroupUserStatus.ACTIVE);
    }

    public boolean isUserAdmin(
            UUID groupId) {

        UUID userId = jwtService.getLoggedId();

        GroupUserEntity groupUser = groupUserRepo
                .findByUser_IdAndGroup_Id(
                        userId,
                        groupId)
                .orElseThrow(() -> new RuntimeException(
                        "User is not a member of the group"));

        if (groupUser.getStatus() != GroupUserStatus.ACTIVE) {

            throw new RuntimeException(
                    "User is not active in this group");
        }

        return groupUser.getRole() == Role.ADMIN;
    }

    public void validateUserInGroup(UUID groupId) {
        UUID userId = jwtService.getLoggedId();

        boolean isMember = groupUserRepo
                .existsByUser_IdAndGroup_IdAndStatus(
                        userId,
                        groupId,
                        GroupUserStatus.ACTIVE);

        if (!isMember) {
            throw new RuntimeException(
                    "User is not a member of this group");
        }
    }

    public void validateAdmin(UUID groupId) {

        validateUserInGroup(groupId);

        if (!isUserAdmin(groupId)) {
            throw new RuntimeException(
                    "Only admins can perform this action");
        }
    }

    public void changeRole(UUID groupId, UUID targetUserId, String newRole) {

        validateAdmin(groupId);

        GroupUserEntity targetMembership = groupUserRepo
                .findByUser_IdAndGroup_Id(targetUserId, groupId)
                .orElseThrow(() -> new RuntimeException("Target user is not in this group"));

        if (targetMembership.getStatus() != GroupUserStatus.ACTIVE) {
            throw new RuntimeException("Target user is not active in this group");
        }

        Role roleToSet;
        try {
            roleToSet = Role.valueOf(newRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role");
        }

        if (targetMembership.getRole() == Role.ADMIN && roleToSet != Role.ADMIN) {

            long adminCount = groupUserRepo.findByGroup_Id(groupId).stream()
                    .filter(u -> u.getStatus() == GroupUserStatus.ACTIVE && u.getRole() == Role.ADMIN)
                    .count();

            if (adminCount <= 1) {
                throw new RuntimeException("Operação negada: A sala precisa ter pelo menos um administrador.");
            }
        }

        targetMembership.setRole(roleToSet);
        groupUserRepo.save(targetMembership);
    }

}
