package fafenterprise.dev.gograduation.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.request.RaffleRequestDTO;
import fafenterprise.dev.gograduation.dto.response.RaffleResponseDTO;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.entity.uno.RaffleEntity;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import fafenterprise.dev.gograduation.repository.RaffleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class RaffleService{
    final RaffleRepository raffleRepo;
    final GroupRepository groupRepo;

    public RaffleResponseDTO create(RaffleRequestDTO raffleRequestDTO){
        RaffleEntity raffle = new RaffleEntity();
        GroupEntity group = groupRepo.findById(raffleRequestDTO.groupId()).orElseThrow();

        raffle.setName(raffleRequestDTO.name());
        raffle.setGroup(group);
        raffle.setValue(raffleRequestDTO.value());
        raffle.setTotal(null);

        raffleRepo.save(raffle);

        return new RaffleResponseDTO(
            raffle.getId(), 
            raffle.getName(), 
            raffle.getGroup().getId(),
            raffle.getValue(), 
            raffle.getTotal()

        );


    }

    public List<RaffleResponseDTO> listAll(){
        return raffleRepo.findAll().stream().map(raffle -> new RaffleResponseDTO(
            raffle.getId(), 
            raffle.getName(),
            raffle.getGroup().getId(), 
            raffle.getValue(), 
            raffle.getTotal()
        )).toList();

    }

    public RaffleResponseDTO update(UUID id, RaffleRequestDTO raffleRequestDTO){
        RaffleEntity raffle = raffleRepo.findById(id).orElseThrow();
        GroupEntity group = groupRepo.findById(raffleRequestDTO.groupId()).orElseThrow();

        raffle.setName(raffleRequestDTO.name());
        raffle.setTotal(raffleRequestDTO.total());
        raffle.setValue(raffleRequestDTO.value());
        raffle.setGroup(group);


        raffleRepo.save(raffle);

        return new RaffleResponseDTO(
            raffle.getId(), 
            raffle.getName(),
            raffle.getGroup().getId(),
            raffle.getValue(), 
            raffle.getTotal()
        );


    }

    public void delete(UUID id){
        RaffleEntity raffle = raffleRepo.findById(id).orElseThrow();
        raffleRepo.delete(raffle);

    }

}