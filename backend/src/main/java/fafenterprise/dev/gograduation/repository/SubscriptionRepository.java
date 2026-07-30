package fafenterprise.dev.gograduation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fafenterprise.dev.gograduation.entity.relationship.SubscriptionEntity;

public interface SubscriptionRepository
        extends JpaRepository<SubscriptionEntity, UUID> {

    List<SubscriptionEntity> findByMonthlyFeeGroupId(UUID groupId);

    Optional<SubscriptionEntity> findByMonthlyFeeIdAndUserId(
            UUID monthlyFeeId,
            UUID userId
    );
}