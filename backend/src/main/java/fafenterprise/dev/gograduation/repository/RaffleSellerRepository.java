package fafenterprise.dev.gograduation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fafenterprise.dev.gograduation.entity.relationship.RaffleSellerEntity;

public interface RaffleSellerRepository
        extends JpaRepository<RaffleSellerEntity, UUID> {

    List<RaffleSellerEntity> findByUserId(UUID userId);

    List<RaffleSellerEntity> findByUserIdAndRaffleGroupId(
            UUID userId,
            UUID groupId
    );
}