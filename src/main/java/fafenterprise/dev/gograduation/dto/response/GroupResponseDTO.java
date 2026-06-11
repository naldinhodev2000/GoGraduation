package fafenterprise.dev.gograduation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record GroupResponseDTO(
        UUID id,
        String name,
        BigDecimal goal,
        String team,
        String token

) {
}
