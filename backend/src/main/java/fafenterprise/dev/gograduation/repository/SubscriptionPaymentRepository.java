
package fafenterprise.dev.gograduation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fafenterprise.dev.gograduation.entity.relationship.SubscriptionPaymentEntity;

public interface SubscriptionPaymentRepository
        extends JpaRepository<SubscriptionPaymentEntity, UUID> {

    List<SubscriptionPaymentEntity> findBySubscriptionId(
            UUID subscriptionId
    );

    List<SubscriptionPaymentEntity> findBySubscriptionMonthlyFeeGroupId(
            UUID groupId
    );
}