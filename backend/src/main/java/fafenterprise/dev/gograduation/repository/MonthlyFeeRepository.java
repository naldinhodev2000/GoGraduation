
package fafenterprise.dev.gograduation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fafenterprise.dev.gograduation.entity.uno.MonthlyFeeEntity;

public interface MonthlyFeeRepository extends JpaRepository<MonthlyFeeEntity, UUID> {

    List<MonthlyFeeEntity> findByGroup_Id(UUID groupId);

    @Query("""
        SELECT m
        FROM MonthlyFeeEntity m
        WHERE m.group.id = :groupId
        AND m.startDate <= :date
        AND (m.endDate IS NULL OR m.endDate >= :date)
        ORDER BY m.startDate DESC
    """)
    List<MonthlyFeeEntity> findCurrentByGroupId(
            @Param("groupId") UUID groupId,
            @Param("date") LocalDate date
    );
}
