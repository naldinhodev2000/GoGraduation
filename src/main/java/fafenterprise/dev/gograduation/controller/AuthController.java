package fafenterprise.dev.gograduation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fafenterprise.dev.gograduation.dto.request.LoginRequestDTO;
import fafenterprise.dev.gograduation.dto.response.LoginResponseDTO;
import fafenterprise.dev.gograduation.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request){
        return new LoginResponseDTO(authService.login(request));

    }
}
