package fafenterprise.dev.gograduation.dto.response;

import java.util.UUID;

public record DetailedMemberDTO(
        UUID userId,
        String name,
        String role
) {}
