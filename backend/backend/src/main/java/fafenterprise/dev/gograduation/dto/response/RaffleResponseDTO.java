package fafenterprise.dev.gograduation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record RaffleResponseDTO(
    UUID id,
    String name, 
    UUID groupId,
    BigDecimal value,
    Integer total

) {
    
}
