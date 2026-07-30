package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.relationship.RaffleSellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RaffleSellerRepository extends JpaRepository<RaffleSellerEntity, UUID> {
}
