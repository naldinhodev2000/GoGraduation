package fafenterprise.dev.gograduation.services;

import fafenterprise.dev.gograduation.repository.GroupUserRepository;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.request.ExpenseRequestDTO;
import fafenterprise.dev.gograduation.dto.response.ExpenseResponseDTO;
import fafenterprise.dev.gograduation.entity.uno.ExpenseEntity;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.repository.ExpenseRepository;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final GroupUserRepository groupUserRepository;
    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final JwtService jwtService;

    public ExpenseResponseDTO create(ExpenseRequestDTO expenseRequestDTO) {

        UUID userId = jwtService.getLoggedId();

        if (!groupUserRepository.existsByUser_IdAndGroup_Id(userId, expenseRequestDTO.groupId())) {
            throw new RuntimeException("User is not a member of the group");
        }

        GroupEntity group = groupRepository.findById(expenseRequestDTO.groupId()).orElseThrow();

        ExpenseEntity expense = new ExpenseEntity();
        expense.setDescription(expenseRequestDTO.description());
        expense.setValue(expenseRequestDTO.value());
        expense.setGroup(group);

        expenseRepository.save(expense);

        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getGroup().getId(),
                expense.getDescription(),
                expense.getValue());
    }

    public ExpenseResponseDTO update(UUID id, ExpenseRequestDTO expenseRequestDTO) {

        UUID userId = jwtService.getLoggedId();

        if (!groupUserRepository.existsByUser_IdAndGroup_Id(userId, expenseRequestDTO.groupId())) {
            throw new RuntimeException("User is not a member of the group");
        }

        ExpenseEntity expense = expenseRepository.findById(id).orElseThrow();
        GroupEntity group = groupRepository.findById(expenseRequestDTO.groupId()).orElseThrow();

        expense.setDescription(expenseRequestDTO.description());
        expense.setGroup(group);
        expense.setValue(expenseRequestDTO.value());

        expenseRepository.save(expense);

        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getGroup().getId(),
                expense.getDescription(),
                expense.getValue());
    }

    public List<ExpenseResponseDTO> listAll() {
        return expenseRepository.findAll().stream()
                .map(expense -> new ExpenseResponseDTO(
                        expense.getId(),
                        expense.getGroup().getId(),
                        expense.getDescription(),
                        expense.getValue()))
                .toList();
    }

    public void delete(UUID id) {

        UUID userId = jwtService.getLoggedId();

        ExpenseEntity expense = expenseRepository.findById(id).orElseThrow();

        if (!groupUserRepository.existsByUser_IdAndGroup_Id(
                userId,
                expense.getGroup().getId())) {

            throw new RuntimeException("User is not a member of the group");
        }

        expenseRepository.delete(expense);
    }

    public List<ExpenseResponseDTO> listByGroupId(UUID groupId) {

        UUID userId = jwtService.getLoggedId();

        if (!groupUserRepository.existsByUser_IdAndGroup_Id(userId, groupId)) {
            throw new RuntimeException("User is not a member of the group");
        }

        return expenseRepository.findByGroup_Id(groupId)
                .stream()
                .map(expense -> new ExpenseResponseDTO(
                        expense.getId(),
                        expense.getGroup().getId(),
                        expense.getDescription(),
                        expense.getValue()))
                .toList();
    }
}