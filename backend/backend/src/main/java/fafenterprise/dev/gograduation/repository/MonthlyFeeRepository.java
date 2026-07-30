package fafenterprise.dev.gograduation.repository;

import fafenterprise.dev.gograduation.entity.uno.MonthlyFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MonthlyFeeRepository extends JpaRepository<MonthlyFeeEntity, UUID> {

    List<MonthlyFeeEntity> findByGroup_Id(UUID groupId);
}
