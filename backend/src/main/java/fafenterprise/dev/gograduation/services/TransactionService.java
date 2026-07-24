package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.request.TransactionDTO;
import fafenterprise.dev.gograduation.entity.uno.Cash;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.entity.uno.TransactionEntity;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.enums.TransactionType;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import fafenterprise.dev.gograduation.repository.TransactionRepository;
import fafenterprise.dev.gograduation.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {

        GroupEntity group = groupRepository
                .findById(transactionDTO.groupId())
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));

        Cash cash = group.getCash();

        UserEntity user = userRepository
                .findById(transactionDTO.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        TransactionEntity transaction = new TransactionEntity();

        transaction.setValue(transactionDTO.value());
        transaction.setDescription(transactionDTO.description());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setType(
                TransactionType.valueOf(transactionDTO.type())
        );
        transaction.setCashRegister(cash);
        transaction.setUser(user);

        transactionRepository.save(transaction);

        return transactionDTO;
    }

    public void remove(UUID id) {
        transactionRepository.deleteById(id);
    }
}