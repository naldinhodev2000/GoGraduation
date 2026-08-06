package fafenterprise.dev.gograduation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import fafenterprise.dev.gograduation.dto.RemainingToGoalDTO;
import fafenterprise.dev.gograduation.dto.request.TransactionDTO;
import fafenterprise.dev.gograduation.dto.response.CashResponseDTO;
import fafenterprise.dev.gograduation.services.CashService;
import fafenterprise.dev.gograduation.services.TransactionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cash")
@RequiredArgsConstructor
public class CashController {

    private final TransactionService transactionService;
    private final CashService cashService;

    @GetMapping("/{groupId}/goal")
    public RemainingToGoalDTO getRemainingToGoal(
            @PathVariable UUID groupId) {

        return cashService.getRemainingToGoal(groupId);
    }

    @GetMapping("/{groupId}")
    public CashResponseDTO getCash(
            @PathVariable UUID groupId) {

        return cashService.getCash(groupId);
    }

    /**
     * Adiciona uma transação ao caixa.
     * Pode ser:
     * - Entrada/Saída comum;
     * - Relacionada a uma rifa;
     * - Relacionada ao pagamento de uma mensalidade.
     */
    @PostMapping("/transactions")
    public TransactionDTO addTransaction(
            @RequestBody TransactionDTO transactionDTO) {

        return transactionService.addTransaction(transactionDTO);
    }

    /**
     * Remove uma transação.
     */
    @DeleteMapping("/transactions/{id}")
    public void deleteTransaction(
            @PathVariable UUID id) {

        transactionService.remove(id);
    }

}