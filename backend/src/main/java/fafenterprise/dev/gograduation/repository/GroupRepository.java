
package fafenterprise.dev.gograduation.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fafenterprise.dev.gograduation.entity.uno.GroupEntity;

public interface GroupRepository
        extends JpaRepository<GroupEntity, UUID> {

    Optional<GroupEntity> findByToken(String token);

    Optional<GroupEntity> findByCash_Id(UUID cashId);
}