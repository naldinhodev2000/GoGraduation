package fafenterprise.dev.gograduation.dto.response;

import java.math.BigDecimal;

public record CashResponseDTO(
    String groupName,
    BigDecimal totalCash
) {
    
}
