package fafenterprise.dev.gograduation.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record ExpenseRequestDTO(
    UUID groupId,
    String description, 
    BigDecimal value


){} 