package fafenterprise.dev.gograduation.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fafenterprise.dev.gograduation.dto.RemainingToGoalDTO;
import fafenterprise.dev.gograduation.dto.response.CashResponseDTO;
import fafenterprise.dev.gograduation.entity.uno.Cash;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.entity.uno.TransactionEntity;
import fafenterprise.dev.gograduation.enums.TransactionType;
import fafenterprise.dev.gograduation.repository.CashRegisterRepository;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CashService {

    private final GroupRepository groupRepository;
    private final CashRegisterRepository cashRegisterRepository;

    public CashResponseDTO getCash(UUID groupId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow();

        return new CashResponseDTO(group.getName(), group.getCash().getValue());
    }

    public void updateCash(UUID groupId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow();

        Cash cash = group.getCash();

        BigDecimal total = calculateCash(cash.getTransactions());

        cash.setValue(total);

        cashRegisterRepository.save(cash);
    }

    private BigDecimal calculateCash(List<TransactionEntity> transactions) {
        BigDecimal total = BigDecimal.ZERO;

        for (TransactionEntity transaction : transactions) {
            if (transaction.getType() == TransactionType.ENTRADA) {
                total = total.add(transaction.getValue());
            } else {
                total = total.subtract(transaction.getValue());
            }
        }

        return total;
    }

    public RemainingToGoalDTO getRemainingToGoal(UUID groupID){
        GroupEntity groupEntity = groupRepository.findById(groupID).orElseThrow();
        return new RemainingToGoalDTO(groupEntity.getName(), groupEntity.getGoal(), getCash(groupID).totalCash());
    }   

}