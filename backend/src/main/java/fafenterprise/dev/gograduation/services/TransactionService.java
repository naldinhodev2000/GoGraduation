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



    public TransactionDTO addTransaction(TransactionDTO dto) {


        UUID groupId = dto.groupId();


        groupUserService.validateUserInGroup(groupId);

        groupUserService.validateAdmin(groupId);



        GroupEntity group =
                groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Grupo não encontrado"));



        Cash cash = group.getCash();


        if (cash == null) {

            throw new RuntimeException(
                    "Caixa não encontrado");

        }



        UserEntity user =
                userRepository.findById(
                        jwtService.getLoggedId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"));



        TransactionEntity transaction =
                new TransactionEntity();



        transaction.setValue(
                dto.value()
        );


        transaction.setDescription(
                dto.description()
        );


        transaction.setCreatedAt(
                LocalDateTime.now()
        );


        transaction.setType(
                TransactionType.valueOf(
                        dto.type().toUpperCase()
                )
        );


        transaction.setCashRegister(
                cash
        );


        transaction.setUser(
                user
        );



        // RIFA

        if (dto.raffleId() != null) {


            RaffleEntity raffle =
                    raffleRepository.findById(
                            dto.raffleId()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Rifa não encontrada"));



            if (!raffle.getGroup()
                    .getId()
                    .equals(groupId)) {


                throw new RuntimeException(
                        "A rifa não pertence a esta comunidade");

            }



            transaction.setRaffle(
                    raffle
            );

        }




        // MENSALIDADE

        if (dto.subscriptionPaymentId() != null) {


            SubscriptionPaymentEntity payment =
                    subscriptionPaymentRepository
                    .findById(
                            dto.subscriptionPaymentId()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Pagamento não encontrado"));



            if (!payment
                    .getSubscription()
                    .getMonthlyFee()
                    .getGroup()
                    .getId()
                    .equals(groupId)) {


                throw new RuntimeException(
                        "Pagamento pertence a outra comunidade");

            }



            transaction.setSubscriptionPayment(
                    payment
            );

        }




        transactionRepository.save(
                transaction
        );


        /*
         * Garante que a transação
         * foi enviada ao banco antes
         * de recalcular o saldo.
         */
        transactionRepository.flush();



        cashService.updateCash(
                groupId
        );



        return dto;

    }





    public void remove(UUID id) {


        TransactionEntity transaction =
                transactionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Transação não encontrada"));



        Cash cash =
                transaction.getCashRegister();



        if (cash == null ||
            cash.getGroup() == null) {


            throw new RuntimeException(
                    "Não foi possível identificar a comunidade");

        }



        UUID groupId =
                cash.getGroup()
                .getId();



        groupUserService.validateUserInGroup(
                groupId
        );


        groupUserService.validateAdmin(
                groupId
        );



        transactionRepository.delete(
                transaction
        );



        transactionRepository.flush();



        cashService.updateCash(
                groupId
        );

    }

}