package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.uno.MonthlyFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MonthlyFeeRepository extends JpaRepository<MonthlyFeeEntity, UUID> {
}
