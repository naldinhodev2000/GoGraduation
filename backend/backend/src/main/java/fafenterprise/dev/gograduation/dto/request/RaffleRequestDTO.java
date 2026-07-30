package fafenterprise.dev.gograduation.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record RaffleRequestDTO(
    String name,
    UUID groupId, 
    BigDecimal value, 
    Integer total
) {
    
}
