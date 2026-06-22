package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.relationship.SubscriptionPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPaymentEntity, UUID> {
}
