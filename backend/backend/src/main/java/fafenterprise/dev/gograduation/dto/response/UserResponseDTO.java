package fafenterprise.dev.gograduation.dto.response;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String nome,
        String email,
        String telefone

) {
}
