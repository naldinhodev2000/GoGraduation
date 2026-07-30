package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.uno.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
}
