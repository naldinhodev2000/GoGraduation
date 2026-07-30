package fafenterprise.dev.gograduation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import fafenterprise.dev.gograduation.enums.GroupUserStatus;
import fafenterprise.dev.gograduation.enums.SubscriptionStatus;

public record MemberSummaryDTO(
        UUID userId,
        String name,
        String email,
        String telefone,
        GroupUserStatus groupStatus,
        SubscriptionStatus subscriptionStatus,
        boolean overdue,
        Integer rafflesSold,
        BigDecimal raffleAmountDue
) {
}