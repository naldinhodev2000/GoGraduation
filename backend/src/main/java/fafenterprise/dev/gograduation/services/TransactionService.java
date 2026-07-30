package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final CashService cashService;
    private final GroupUserService groupUserService;

    public TransactionDTO addTransaction(
            TransactionDTO transactionDTO) {

        UUID groupId = transactionDTO.groupId();

        // Verifica se o usuário logado pertence à sala
        groupUserService.validateUserInGroup(groupId);

        GroupEntity group = groupRepository
                .findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Grupo não encontrado"));

        Cash cash = group.getCash();

        UserEntity user = userRepository
                .findById(transactionDTO.userId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"));

        // Verifica se o usuário da transação pertence à sala
        if (!groupUserService.isUserInGroup(
                groupId,
                user.getId())) {

            throw new RuntimeException(
                    "Usuário não pertence a esta sala");
        }

        TransactionEntity transaction =
                new TransactionEntity();

        transaction.setValue(
                transactionDTO.value());

        transaction.setDescription(
                transactionDTO.description());

        transaction.setCreatedAt(
                LocalDateTime.now());

        transaction.setType(
                TransactionType.valueOf(
                        transactionDTO.type()));

        transaction.setCashRegister(cash);
        transaction.setUser(user);

        transactionRepository.save(transaction);

        cashService.updateCash(groupId);

        return transactionDTO;
    }

    public void remove(UUID id) {

        TransactionEntity transaction =
                transactionRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transação não encontrada"));

        UUID cashId = transaction
                .getCashRegister()
                .getId();

        // Descobre a sala através do caixa
        GroupEntity group = groupRepository
                .findByCashId(cashId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Grupo não encontrado"));

        UUID groupId = group.getId();

        // Usuário logado precisa ser membro da sala
        groupUserService.validateUserInGroup(groupId);

        // Somente admin pode excluir
        groupUserService.validateAdmin(groupId);

        transactionRepository.delete(transaction);

        // Recalcula o saldo após excluir
        cashService.updateCash(groupId);
    }
}