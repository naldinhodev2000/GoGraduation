package fafenterprise.dev.gograduation.dto.request;

import java.util.UUID;

public record RaffleSellerRequestDTO(
        UUID raffleId,
        UUID userId,
        Integer quantity
) {
}