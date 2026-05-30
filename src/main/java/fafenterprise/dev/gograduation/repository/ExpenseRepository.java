package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.uno.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {
}