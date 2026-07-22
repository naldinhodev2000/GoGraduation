package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.response.TransactionDTO;
import fafenterprise.dev.gograduation.entity.uno.TransactionEntity;
import fafenterprise.dev.gograduation.enums.TransactionType;
import fafenterprise.dev.gograduation.repository.CashRegisterRepository;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import fafenterprise.dev.gograduation.repository.TransactionRepository;
import fafenterprise.dev.gograduation.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CashService {
    private final TransactionRepository cashRegisterRepo;
    final GroupRepository groupRepository;
    final UserRepository userRepository;

    public TransactionEntity addTransaction(TransactionDTO transactionDTO){
        TransactionEntity transaction = new TransactionEntity();

        transaction.setValue(transactionDTO.value());
        transaction.setDescription(transactionDTO.description());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setType(TransactionType.valueOf(transactionDTO.type()));
        transaction.setGroup(groupRepository.findById(transactionDTO.groupId()).orElseThrow());
        transaction.setUser(userRepository.findById(transactionDTO.userId()).orElseThrow());

        return cashRegisterRepo.save(transaction);
    }

    public void remove(UUID id){
        cashRegisterRepo.deleteById(id);
    }
    
}
