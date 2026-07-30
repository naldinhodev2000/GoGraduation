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
    private final GroupUserService groupUserService;

    /*
     * Consulta o caixa da comunidade.
     *
     * Somente usuários que pertencem à comunidade
     * podem consultar.
     */
    @Transactional(readOnly = true)
    public CashResponseDTO getCash(UUID groupId) {

        groupUserService.validateUserInGroup(groupId);

        GroupEntity group = groupRepository
                .findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Grupo não encontrado"));

        Cash cash = group.getCash();

        if (cash == null) {
            throw new RuntimeException(
                    "Caixa não encontrado");
        }

        return new CashResponseDTO(
                group.getName(),
                cash.getValue()
        );
    }

    /*
     * Atualiza o saldo do caixa.
     *
     * Este método é utilizado internamente
     * após uma movimentação ser criada ou removida.
     */
    public void updateCash(UUID groupId) {

        GroupEntity group = groupRepository
                .findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Grupo não encontrado"));

        Cash cash = group.getCash();

        if (cash == null) {
            throw new RuntimeException(
                    "Caixa não encontrado");
        }

        BigDecimal total =
                calculateCash(cash.getTransactions());

        cash.setValue(total);

        cashRegisterRepository.save(cash);
    }

    /*
     * Calcula o saldo:
     *
     * ENTRADA -> soma
     * SAÍDA   -> subtrai
     */
    private BigDecimal calculateCash(
            List<TransactionEntity> transactions) {

        BigDecimal total =
                BigDecimal.ZERO;

        if (transactions == null) {
            return total;
        }

        for (TransactionEntity transaction :
                transactions) {

            if (transaction.getType() ==
                    TransactionType.ENTRADA) {

                total = total.add(
                        transaction.getValue());

            } else {

                total = total.subtract(
                        transaction.getValue());
            }
        }

        return total;
    }

    /*
     * Consulta quanto falta para atingir
     * a meta da comunidade.
     *
     * Somente membros da comunidade
     * podem consultar.
     */
    @Transactional(readOnly = true)
    public RemainingToGoalDTO getRemainingToGoal(
            UUID groupId) {

        groupUserService.validateUserInGroup(groupId);

        GroupEntity groupEntity =
                groupRepository
                        .findById(groupId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Grupo não encontrado"));

        CashResponseDTO cash =
                getCash(groupId);

        return new RemainingToGoalDTO(
                groupEntity.getName(),
                groupEntity.getGoal(),
                cash.totalCash()
        );
    }
}