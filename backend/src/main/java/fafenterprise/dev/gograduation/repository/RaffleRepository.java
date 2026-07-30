
package fafenterprise.dev.gograduation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fafenterprise.dev.gograduation.entity.uno.RaffleEntity;

public interface RaffleRepository
        extends JpaRepository<RaffleEntity, UUID> {

    List<RaffleEntity> findByGroup_Id(UUID groupId);
}