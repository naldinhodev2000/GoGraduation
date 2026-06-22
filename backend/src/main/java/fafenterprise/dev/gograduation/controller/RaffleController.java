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

import fafenterprise.dev.gograduation.dto.request.RaffleRequestDTO;
import fafenterprise.dev.gograduation.dto.response.RaffleResponseDTO;
import fafenterprise.dev.gograduation.services.RaffleService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class RaffleController {

    final RaffleService raffleService;


    @PostMapping
    public RaffleResponseDTO create(@RequestBody RaffleRequestDTO raffleRequestDTO){
        return raffleService.create(raffleRequestDTO);
    }
    
    @GetMapping
    public List<RaffleResponseDTO> listAll(){
        return raffleService.listAll();
    }

    @PutMapping("/{id}")
    public RaffleResponseDTO update(@PathVariable UUID id, @RequestBody RaffleRequestDTO raffleRequestDTO){
        return raffleService.update(id, raffleRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        raffleService.delete(id);
    }
}
