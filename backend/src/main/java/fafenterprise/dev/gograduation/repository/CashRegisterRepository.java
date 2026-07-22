package fafenterprise.dev.gograduation.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fafenterprise.dev.gograduation.entity.uno.Cash;

public interface CashRegisterRepository extends JpaRepository<Cash, UUID>{
    
    
}
