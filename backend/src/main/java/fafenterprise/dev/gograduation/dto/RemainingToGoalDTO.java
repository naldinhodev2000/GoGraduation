package fafenterprise.dev.gograduation.dto;

import java.math.BigDecimal;

/**
 * RemainingToGoalDTO
 */
public record RemainingToGoalDTO(
    String nameGroupString, 
    BigDecimal goal, 
    BigDecimal balance
) {
    
}
