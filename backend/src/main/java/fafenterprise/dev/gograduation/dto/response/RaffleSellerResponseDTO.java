
package fafenterprise.dev.gograduation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record RaffleSellerResponseDTO(
        UUID id,
        UUID raffleId,
        UUID userId,
        Integer quantity,
        BigDecimal amountDue
) {
}