
package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fafenterprise.dev.gograduation.dto.response.SubscriptionPaymentResponseDTO;
import fafenterprise.dev.gograduation.entity.relationship.SubscriptionEntity;
import fafenterprise.dev.gograduation.entity.relationship.SubscriptionPaymentEntity;
import fafenterprise.dev.gograduation.repository.SubscriptionPaymentRepository;
import fafenterprise.dev.gograduation.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionPaymentService {

        private final SubscriptionPaymentRepository paymentRepository;
        private final SubscriptionRepository subscriptionRepository;
        private final GroupUserService groupUserService;

        public SubscriptionPaymentEntity create(
                        UUID subscriptionId,
                        SubscriptionPaymentEntity payment) {

                SubscriptionEntity subscription = subscriptionRepository
                                .findById(subscriptionId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Subscription not found"));

                UUID groupId = subscription
                                .getMonthlyFee()
                                .getGroup()
                                .getId();

                validateMember(groupId);

                validateAdmin(groupId);

                payment.setId(null);
                payment.setSubscription(subscription);

                if (payment.getDate() == null) {
                        payment.setDate(
                                        LocalDateTime.now());
                }

                return paymentRepository.save(payment);
        }

        @Transactional(readOnly = true)
        public List<SubscriptionPaymentEntity> listBySubscription(
                        UUID subscriptionId) {

                SubscriptionEntity subscription = subscriptionRepository
                                .findById(subscriptionId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Subscription not found"));

                UUID groupId = subscription
                                .getMonthlyFee()
                                .getGroup()
                                .getId();

                validateMember(groupId);

                return paymentRepository
                                .findBySubscriptionId(subscriptionId);
        }

        @Transactional(readOnly = true)
        public List<SubscriptionPaymentResponseDTO> listByGroup(UUID groupId) {

                validateMember(groupId);

                return paymentRepository
                                .findBySubscriptionMonthlyFeeGroupId(groupId)
                                .stream()
                                .map(payment -> new SubscriptionPaymentResponseDTO(

                                                payment.getId(),

                                                payment.getSubscription()
                                                                .getUser()
                                                                .getName(),

                                                payment.getReference(),

                                                payment.getValue()

                                ))
                                .toList();
        }

        public void delete(UUID paymentId) {

                SubscriptionPaymentEntity payment = paymentRepository
                                .findById(paymentId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Payment not found"));

                UUID groupId = payment
                                .getSubscription()
                                .getMonthlyFee()
                                .getGroup()
                                .getId();

                validateMember(groupId);
                validateAdmin(groupId);

                paymentRepository.delete(payment);
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
