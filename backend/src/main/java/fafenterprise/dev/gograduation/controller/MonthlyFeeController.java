package fafenterprise.dev.gograduation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fafenterprise.dev.gograduation.dto.MonthlyFeeDTO;
import fafenterprise.dev.gograduation.services.MonthlyFeeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/monthly-fees")
@RequiredArgsConstructor
public class MonthlyFeeController {

    private final MonthlyFeeService monthlyFeeService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody MonthlyFeeDTO monthlyFeeDTO) {

        monthlyFeeService.create(monthlyFeeDTO);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable UUID id,
            @RequestBody MonthlyFeeDTO monthlyFeeDTO) {

        monthlyFeeService.update(id, monthlyFeeDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MonthlyFeeDTO>> getAllByGroupId(
            @PathVariable UUID groupId) {

        return ResponseEntity.ok(
                monthlyFeeService.getAllByGroupId(groupId)
        );
    }
}