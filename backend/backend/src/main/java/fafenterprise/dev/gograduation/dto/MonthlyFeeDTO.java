package fafenterprise.dev.gograduation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MonthlyFeeDTO(
    UUID id,
    BigDecimal value, 
    UUID groupId,
    LocalDate startDate,
    LocalDate endDate 

) {
}