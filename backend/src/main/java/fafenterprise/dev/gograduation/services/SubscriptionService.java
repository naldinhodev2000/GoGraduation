
package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class SubscriptionService {

    private final MonthlyFeeRepository monthlyFeeRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final GroupUserService groupUserService;


    public SubscriptionEntity subscribe(
            SubscriptionDTO subscriptionDTO) {

        MonthlyFeeEntity monthlyFee =
                monthlyFeeRepository
                        .findById(subscriptionDTO.monthlyFeeId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Monthly fee not found"));

        UUID groupId =
                monthlyFee
                        .getGroup()
                        .getId();

        validateMember(groupId);

        validateAdmin(groupId);

        UserEntity user =
                userRepository
                        .findById(subscriptionDTO.userId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!groupUserService.isUserInGroup(
                groupId,
                user.getId())) {

            throw new RuntimeException(
                    "User is not a member of this group");
        }

        SubscriptionEntity subscription =
                new SubscriptionEntity();

        subscription.setMonthlyFee(monthlyFee);
        subscription.setUser(user);
        subscription.setSubscriptionDate(
                LocalDateTime.now());

        subscription.setStatus(
                SubscriptionStatus.ACTIVE);

        return subscriptionRepository.save(
                subscription);
    }

    public void changeStatus(
            UUID subscriptionId,
            SubscriptionStatus status) {

        SubscriptionEntity subscription =
                subscriptionRepository
                        .findById(subscriptionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subscription not found"));

        UUID groupId =
                subscription
                        .getMonthlyFee()
                        .getGroup()
                        .getId();

        validateMember(groupId);
        validateAdmin(groupId);

        subscription.setStatus(status);

        subscriptionRepository.save(
                subscription);
    }

    public List<SubscriptionEntity> listByGroup(
            UUID groupId) {

        validateMember(groupId);

        return subscriptionRepository
                .findByMonthlyFeeGroupId(groupId);
    }

    private void validateMember(
            UUID groupId) {

        if (!groupUserService.isUserInGroup(
                groupId)) {

            throw new RuntimeException(
                    "User is not a member of the group");
        }
    }

    private void validateAdmin(
            UUID groupId) {

        if (!groupUserService.isUserAdmin(
                groupId)) {

            throw new RuntimeException(
                    "Only admins can perform this action");
        }
    }
}
