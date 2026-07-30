package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.request.LoginRequestDTO;
import fafenterprise.dev.gograduation.dto.request.UserRequestDTO;
import fafenterprise.dev.gograduation.dto.response.UserResponseDTO;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public String login(LoginRequestDTO request) {

        UserEntity userEntity = userRepo.findByLoginOrEmail(request.user().trim(), request.user().trim())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.password(), userEntity.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(userEntity);
    }



    public UserResponseDTO register(UserRequestDTO request) {
        UserEntity usuario = new UserEntity();

        usuario.setName(request.name());
        usuario.setEmail(request.email());
        usuario.setTelefone(request.telefone());
        usuario.setLogin(request.login());
        usuario.setPassword(encoder.encode(request.senha()));
        usuario.setCreatedAt(LocalDateTime.now());
        usuario.setUpdatedAt(LocalDateTime.now());

        UserEntity novoUsuario = userRepo.save(usuario);

        return new UserResponseDTO(
            novoUsuario.getId(),
            novoUsuario.getName(),
            novoUsuario.getEmail(),
            novoUsuario.getTelefone());
    }
}
