package fafenterprise.dev.gograduation.dto.request;


public record UserRequestDTO(
        String name,
        String email,
        String telefone,
        String login,
        String senha
) {
}

