package fafenterprise.dev.gograduation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fafenterprise.dev.gograduation.dto.response.TransactionDTO;
import fafenterprise.dev.gograduation.entity.uno.TransactionEntity;
import fafenterprise.dev.gograduation.services.CashService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cash")
@RequiredArgsConstructor
public class CashController {
    final CashService cashService;
    @PostMapping()
    public TransactionDTO addTransaction(@RequestBody TransactionDTO transaction) {
        return cashService.addTransaction(transaction);
        
        
    }


    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable UUID id){
       cashService.remove(id);
    }


    
    
}
