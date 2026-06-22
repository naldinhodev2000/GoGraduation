package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.uno.RaffleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RaffleRepository extends JpaRepository<RaffleEntity, UUID> {
}
