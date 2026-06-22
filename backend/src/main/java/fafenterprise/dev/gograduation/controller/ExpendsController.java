package fafenterprise.dev.gograduation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fafenterprise.dev.gograduation.dto.request.ExpenseRequestDTO;
import fafenterprise.dev.gograduation.dto.response.ExpenseResponseDTO;
import fafenterprise.dev.gograduation.services.ExpenseService;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/expends")
@RequiredArgsConstructor
public class ExpendsController{
    final ExpenseService expenseService;
    
    @PostMapping
    public ExpenseResponseDTO create(@RequestBody ExpenseRequestDTO expenseRequestDTO){
        return expenseService.create(expenseRequestDTO);
    }

    @PutMapping("/{id}")
    public ExpenseResponseDTO update(@PathVariable UUID id, @RequestBody ExpenseRequestDTO expense){
        return expenseService.update(id, expense);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        expenseService.delete(id);
    }

    @GetMapping("/{groupId}/expenses")
    public List<ExpenseResponseDTO> listByGroupId(@PathVariable UUID groupId){
        return expenseService.listByGroupId(groupId);
    }

    
}