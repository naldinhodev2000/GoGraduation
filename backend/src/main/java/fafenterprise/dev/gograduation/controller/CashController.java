package fafenterprise.dev.gograduation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/{groupId}")
    public CashResponseDTO getCash(@PathVariable UUID groupId) {
        return cashService.getCash(groupId);
    }

  
    @PostMapping
    public TransactionDTO addTransaction(
            @RequestBody TransactionDTO transaction) {

        return transactionService.addTransaction(transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(
            @PathVariable UUID id) {

        transactionService.remove(id);
    }
}