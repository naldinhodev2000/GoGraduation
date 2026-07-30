
package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.SubscriptionDTO;
import fafenterprise.dev.gograduation.entity.relationship.SubscriptionEntity;
import fafenterprise.dev.gograduation.entity.uno.MonthlyFeeEntity;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.enums.SubscriptionStatus;
import fafenterprise.dev.gograduation.repository.MonthlyFeeRepository;
import fafenterprise.dev.gograduation.repository.SubscriptionRepository;
import fafenterprise.dev.gograduation.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final MonthlyFeeRepository monthlyFeeRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionEntity subscribe(SubscriptionDTO subscriptionDTO) {

        MonthlyFeeEntity monthlyFee = monthlyFeeRepository
                .findById(subscriptionDTO.monthlyFeeId())
                .orElseThrow();

        UserEntity user = userRepository
                .findById(subscriptionDTO.userId())
                .orElseThrow();

        SubscriptionEntity subscription = new SubscriptionEntity();

        subscription.setMonthlyFee(monthlyFee);
        subscription.setUser(user);
        subscription.setSubscriptionDate(LocalDateTime.now());
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        return subscriptionRepository.save(subscription);
    }

    public void changeStatus(
            UUID subscriptionId,
            SubscriptionStatus status) {

        SubscriptionEntity subscriptionEntity = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow();

        subscriptionEntity.setStatus(status);

        subscriptionRepository.save(subscriptionEntity);
    }

    public List<SubscriptionEntity> listAll() {
        return subscriptionRepository.findAll();
    }

    public List<SubscriptionEntity> listByGroup(UUID groupId) {
        return subscriptionRepository
                .findByMonthlyFeeGroupId(groupId);
    }
}
