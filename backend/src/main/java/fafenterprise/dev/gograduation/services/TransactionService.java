package fafenterprise.dev.gograduation.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fafenterprise.dev.gograduation.dto.request.TransactionDTO;
import fafenterprise.dev.gograduation.entity.relationship.SubscriptionPaymentEntity;
import fafenterprise.dev.gograduation.entity.uno.Cash;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.entity.uno.RaffleEntity;
import fafenterprise.dev.gograduation.entity.uno.TransactionEntity;
import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.enums.TransactionType;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import fafenterprise.dev.gograduation.repository.RaffleRepository;
import fafenterprise.dev.gograduation.repository.SubscriptionPaymentRepository;
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
    private final RaffleRepository raffleRepository;
    private final SubscriptionPaymentRepository subscriptionPaymentRepository;
    private final CashService cashService;
    private final GroupUserService groupUserService;
    private final JwtService jwtService;

    public TransactionDTO addTransaction(
            TransactionDTO transactionDTO) {

        UUID groupId = transactionDTO.groupId();

        // Usuário precisa ser membro da comunidade
        groupUserService.validateUserInGroup(groupId);

        // Somente ADMIN pode lançar movimentações
        groupUserService.validateAdmin(groupId);

        GroupEntity group = groupRepository
                .findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Grupo não encontrado"));

        Cash cash = group.getCash();

        if (cash == null) {
            throw new RuntimeException(
                    "Caixa da comunidade não encontrado");
        }

        // Usuário logado através do JWT
        UUID loggedUserId = jwtService.getLoggedId();

        UserEntity user = userRepository
                .findById(loggedUserId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"));

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
                        transactionDTO.type()
                                .toUpperCase()));

        transaction.setCashRegister(cash);

        transaction.setUser(user);

        /*
         * Vincula a movimentação a uma RIFA.
         */
        if (transactionDTO.raffleId() != null) {

            RaffleEntity raffle =
                    raffleRepository
                            .findById(
                                    transactionDTO.raffleId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Rifa não encontrada"));

            if (!raffle.getGroup()
                    .getId()
                    .equals(groupId)) {

                throw new RuntimeException(
                        "A rifa não pertence a esta comunidade");
            }

            transaction.setRaffle(raffle);
        }

        /*
         * Vincula a movimentação a um
         * pagamento de mensalidade.
         */
        if (transactionDTO.subscriptionPaymentId()
                != null) {

            SubscriptionPaymentEntity payment =
                    subscriptionPaymentRepository
                            .findById(
                                    transactionDTO
                                            .subscriptionPaymentId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Pagamento não encontrado"));

            UUID paymentGroupId =
                    payment
                            .getSubscription()
                            .getMonthlyFee()
                            .getGroup()
                            .getId();

            if (!paymentGroupId.equals(groupId)) {

                throw new RuntimeException(
                        "O pagamento não pertence a esta comunidade");
            }

            transaction.setSubscriptionPayment(
                    payment);
        }

        transactionRepository.save(transaction);

        // Recalcula o saldo do caixa
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

        Cash cash = transaction.getCashRegister();

        if (cash == null || cash.getGroup() == null) {
            throw new RuntimeException(
                    "Não foi possível identificar a comunidade");
        }

        UUID groupId = cash
                .getGroup()
                .getId();

        // Usuário precisa pertencer à comunidade
        groupUserService.validateUserInGroup(groupId);

        // Somente ADMIN pode excluir
        groupUserService.validateAdmin(groupId);

        transactionRepository.delete(transaction);

        // Atualiza o saldo após excluir
        cashService.updateCash(groupId);
    }
}