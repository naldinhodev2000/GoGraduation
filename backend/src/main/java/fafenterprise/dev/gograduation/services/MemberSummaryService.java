package fafenterprise.dev.gograduation.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.response.MemberSummaryDTO;
import fafenterprise.dev.gograduation.entity.relationship.GroupUserEntity;
import fafenterprise.dev.gograduation.entity.relationship.RaffleSellerEntity;
import fafenterprise.dev.gograduation.entity.relationship.SubscriptionEntity;
import fafenterprise.dev.gograduation.entity.uno.MonthlyFeeEntity;
import fafenterprise.dev.gograduation.enums.SubscriptionStatus;
import fafenterprise.dev.gograduation.repository.GroupUserRepository;
import fafenterprise.dev.gograduation.repository.MonthlyFeeRepository;
import fafenterprise.dev.gograduation.repository.RaffleSellerRepository;
import fafenterprise.dev.gograduation.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberSummaryService {

    private final GroupUserRepository groupUserRepository;
    private final RaffleSellerRepository raffleSellerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MonthlyFeeRepository monthlyFeeRepository;

    public MemberSummaryDTO getSummary(
            UUID groupId,
            UUID userId) {

        GroupUserEntity groupUser = groupUserRepository
                .findByUser_IdAndGroup_Id(userId, groupId)
                .orElseThrow(() ->
                        new RuntimeException("User is not a member of this group"));

        List<RaffleSellerEntity> raffleSellers =
                raffleSellerRepository
                        .findByUserIdAndRaffleGroupId(userId, groupId);

        int rafflesSold = raffleSellers.stream()
                .mapToInt(seller ->
                        seller.getQuantity() != null
                                ? seller.getQuantity()
                                : 0)
                .sum();

        BigDecimal raffleAmountDue = raffleSellers.stream()
                .map(RaffleSellerEntity::getAmountDue)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        MonthlyFeeEntity currentMonthlyFee =
        monthlyFeeRepository
                .findCurrentByGroupId(groupId, LocalDate.now())
                .stream()
                .findFirst()
                .orElse(null);

        SubscriptionEntity subscription = null;

        if (currentMonthlyFee != null) {

            subscription = subscriptionRepository
                    .findByMonthlyFeeIdAndUserId(
                            currentMonthlyFee.getId(),
                            userId
                    )
                    .orElse(null);
        }

        UUID subscriptionId =
                subscription != null
                        ? subscription.getId()
                        : null;

        SubscriptionStatus subscriptionStatus =
                subscription != null
                        ? subscription.getStatus()
                        : null;

        boolean overdue = isOverdue(
                currentMonthlyFee,
                subscription
        );

        return new MemberSummaryDTO(
                groupUser.getUser().getId(),
                groupUser.getUser().getName(),
                groupUser.getUser().getEmail(),
                groupUser.getUser().getTelefone(),
                groupUser.getStatus(),
                subscriptionId,
                subscriptionStatus,
                overdue,
                rafflesSold,
                raffleAmountDue
        );
    }

    private boolean isOverdue(
            MonthlyFeeEntity monthlyFee,
            SubscriptionEntity subscription) {

        if (monthlyFee == null) {
            return false;
        }

        if (subscription == null) {
            return true;
        }

        if (subscription.getStatus() == SubscriptionStatus.SUSPENDED
                || subscription.getStatus() == SubscriptionStatus.CANCELED) {

            return true;
        }

        if (monthlyFee.getEndDate() != null
                && monthlyFee.getEndDate().isBefore(LocalDate.now())) {

            return true;
        }

        return false;
    }
}
