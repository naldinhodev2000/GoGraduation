package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.relationship.GroupUserEntity;
import fafenterprise.dev.gograduation.enums.GroupUserStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupUserRepository extends JpaRepository<GroupUserEntity, UUID> {

        Optional<GroupUserEntity> findByUser_IdAndGroup_Id(
                        UUID userId,
                        UUID groupId);

        boolean existsByUser_IdAndGroup_Id(
                        UUID userId,
                        UUID groupId);

        List<GroupUserEntity> findByGroup_Id(
                        UUID groupId);

        List<GroupUserEntity> findByUserIdAndStatus(UUID userId, GroupUserStatus status);
}