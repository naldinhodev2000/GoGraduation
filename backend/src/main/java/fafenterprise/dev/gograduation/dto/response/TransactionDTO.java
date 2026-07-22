package fafenterprise.dev.gograduation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.enums.TransactionType;

public record TransactionDTO(
    BigDecimal value,
    String description,
    String type,
    UUID groupId,
    UUID userId

) {
} 