package fafenterprise.dev.gograduation.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(
        BigDecimal value,
        String description,
        String type,
        UUID groupId,
        UUID raffleId,
        UUID subscriptionPaymentId

) {
}
