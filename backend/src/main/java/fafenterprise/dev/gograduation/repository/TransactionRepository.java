package fafenterprise.dev.gograduation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fafenterprise.dev.gograduation.entity.uno.TransactionEntity;

public interface TransactionRepository
        extends JpaRepository<TransactionEntity, UUID> {

    List<TransactionEntity> findByCashRegister_Group_Id(UUID groupId);

    List<TransactionEntity> findByRaffle_Id(UUID raffleId);

    List<TransactionEntity> findBySubscriptionPayment_Id(UUID paymentId);

}