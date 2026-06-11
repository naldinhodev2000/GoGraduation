package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {

    Optional<GroupEntity> findByToken(String groupId);
}
