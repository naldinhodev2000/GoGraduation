package fafenterprise.dev.gograduation.dto;

import java.util.UUID;

public record SubscriptionDTO(
    UUID monthlyFeeId,
    UUID userId
) {
    
}
