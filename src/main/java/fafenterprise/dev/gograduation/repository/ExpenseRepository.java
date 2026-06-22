package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.dto.response.ExpenseResponseDTO;
import fafenterprise.dev.gograduation.entity.uno.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {

    List<ExpenseEntity> findByGroup_Id(UUID groupId);
}