package fafenterprise.dev.gograduation.services;

import fafenterprise.dev.gograduation.dto.request.UserRequestDTO;
import fafenterprise.dev.gograduation.dto.response.UserResponseDTO;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    public UserResponseDTO create(UserRequestDTO request) {
        UserEntity usuario = new UserEntity();

        usuario.setName(request.name());
        usuario.setEmail(request.email());
        usuario.setTelefone(request.telefone());
        usuario.setLogin(request.login());
        usuario.setPassword(passwordEncoder.encode(request.senha()));
        usuario.setCreatedAt(LocalDateTime.now());
        usuario.setUpdatedAt(LocalDateTime.now());

        UserEntity novoUsuario = userRepository.save(usuario);

        return new UserResponseDTO(
            novoUsuario.getId(),
            novoUsuario.getName(),
            novoUsuario.getEmail(),
            novoUsuario.getTelefone());
    }

    public UserResponseDTO update(UUID id, UserRequestDTO request) {
        UserEntity usuario = userRepository.findById(id).orElseThrow();

        usuario.setName(request.name());
        usuario.setEmail(request.email());
        usuario.setTelefone(request.telefone());
        usuario.setLogin(request.login());
         usuario.setPassword(passwordEncoder.encode(request.senha()));
        usuario.setUpdatedAt(LocalDateTime.now());

        UserEntity usuarioAtualizado = userRepository.save(usuario);

        return new UserResponseDTO(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getName(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getTelefone()
        );

    }
    public List<UserResponseDTO> listAll(){
        return userRepository.findAll()
            .stream()
            .map(usuario -> new UserResponseDTO(
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                usuario.getTelefone()))
                .toList();

    }

    public void delete(UUID id) {
        UserEntity usuario = userRepository.findById(id)
                .orElseThrow();

        userRepository.delete(usuario);
    }




}
