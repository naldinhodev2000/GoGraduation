package fafenterprise.dev.gograduation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import fafenterprise.dev.gograduation.entity.uno.GroupEntity;

public record MonthlyFeeDTO(
    UUID id,
    BigDecimal value, 
    UUID groupId,
    LocalDate startDate,
    LocalDate endDate 

) {
}