
package fafenterprise.dev.gograduation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fafenterprise.dev.gograduation.entity.relationship.SubscriptionEntity;

public interface SubscriptionRepository
        extends JpaRepository<SubscriptionEntity, UUID> {

    // Busca todas as assinaturas de uma sala
    List<SubscriptionEntity> findByMonthlyFeeGroupId(
            UUID groupId
    );

    // Verifica se um usuário já está inscrito
    // em uma determinada mensalidade
    Optional<SubscriptionEntity> findByMonthlyFeeIdAndUserId(
            UUID monthlyFeeId,
            UUID userId
    );
}