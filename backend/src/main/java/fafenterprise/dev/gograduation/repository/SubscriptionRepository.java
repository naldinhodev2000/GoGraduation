package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.relationship.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {
}
