package fafenterprise.dev.gograduation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record SubscriptionPaymentResponseDTO(

        UUID id,

        String user,

        String reference,

        BigDecimal value

) {
}