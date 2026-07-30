package fafenterprise.dev.gograduation.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record GroupRequestDTO(
        String name,
        BigDecimal goal,
        String team,
        String course,
        UUID createdBy
) {

}
