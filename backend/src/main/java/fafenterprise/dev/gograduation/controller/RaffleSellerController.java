package fafenterprise.dev.gograduation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fafenterprise.dev.gograduation.dto.request.RaffleSellerRequestDTO;
import fafenterprise.dev.gograduation.dto.response.RaffleSellerResponseDTO;
import fafenterprise.dev.gograduation.services.RaffleSellerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/raffle-sellers")
@RequiredArgsConstructor
public class RaffleSellerController {

    private final RaffleSellerService raffleSellerService;

    @PostMapping
    public RaffleSellerResponseDTO create(
            @RequestBody RaffleSellerRequestDTO request) {

        return raffleSellerService.create(request);
    }

    @GetMapping("/{id}")
    public RaffleSellerResponseDTO findById(
            @PathVariable UUID id) {

        return raffleSellerService.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<RaffleSellerResponseDTO> findByUser(
            @PathVariable UUID userId) {

        return raffleSellerService.findByUser(userId);
    }

    @GetMapping("/group/{groupId}/user/{userId}")
    public List<RaffleSellerResponseDTO> findByUserAndGroup(
            @PathVariable UUID groupId,
            @PathVariable UUID userId) {

        return raffleSellerService.findByUserAndGroup(
                groupId,
                userId
        );
    }

    @PutMapping("/{id}")
    public RaffleSellerResponseDTO update(
            @PathVariable UUID id,
            @RequestBody RaffleSellerRequestDTO request) {

        return raffleSellerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id) {

        raffleSellerService.delete(id);
    }
}