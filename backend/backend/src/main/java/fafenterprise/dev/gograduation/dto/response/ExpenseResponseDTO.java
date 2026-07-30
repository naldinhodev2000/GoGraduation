package fafenterprise.dev.gograduation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ExpenseResponseDTO(
    UUID id, 
    UUID groupID, 
    String description,
    BigDecimal value

) {
}