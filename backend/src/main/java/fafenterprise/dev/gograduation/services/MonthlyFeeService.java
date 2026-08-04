
package fafenterprise.dev.gograduation.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fafenterprise.dev.gograduation.dto.MonthlyFeeDTO;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.entity.uno.MonthlyFeeEntity;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import fafenterprise.dev.gograduation.repository.GroupUserRepository;
import fafenterprise.dev.gograduation.repository.MonthlyFeeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MonthlyFeeService {

    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final MonthlyFeeRepository monthlyFeeRepository;
    private final JwtService jwtService;
    private final GroupUserService groupUserService;

    public void create(MonthlyFeeDTO monthlyFeeDTO) {

        UUID groupId = monthlyFeeDTO.groupId();

        if (!groupUserService.isUserInGroup(groupId)) {
            throw new RuntimeException(
                    "User is not a member of the group");
        }

        if (!groupUserService.isUserAdmin(groupId)) {
            throw new RuntimeException(
                    "Only admins can create monthly fees");
        }

        GroupEntity group = groupRepository
                .findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        MonthlyFeeEntity monthlyFee =
                new MonthlyFeeEntity();

        monthlyFee.setValue(monthlyFeeDTO.value());
        monthlyFee.setStartDate(monthlyFeeDTO.startDate());
        monthlyFee.setEndDate(monthlyFeeDTO.endDate());
        monthlyFee.setGroup(group);

        monthlyFeeRepository.save(monthlyFee);
    }

    public void update(
            UUID id,
            MonthlyFeeDTO monthlyFeeDTO) {

        MonthlyFeeEntity monthlyFee =
                monthlyFeeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Monthly fee not found"));

        UUID currentGroupId =
                monthlyFee.getGroup().getId();

        if (!groupUserService.isUserInGroup(
                currentGroupId)) {

            throw new RuntimeException(
                    "User is not a member of the group");
        }

        if (!groupUserService.isUserAdmin(
                currentGroupId)) {

            throw new RuntimeException(
                    "Only admins can update monthly fees");
        }

        UUID newGroupId =
                monthlyFeeDTO.groupId();

        if (!currentGroupId.equals(newGroupId)) {

            if (!groupUserService.isUserInGroup(
                    newGroupId)) {

                throw new RuntimeException(
                        "User is not a member of the new group");
            }

            if (!groupUserService.isUserAdmin(
                    newGroupId)) {

                throw new RuntimeException(
                        "User is not an admin of the new group");
            }
        }

        GroupEntity group =
                groupRepository
                        .findById(newGroupId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Group not found"));

        monthlyFee.setValue(
                monthlyFeeDTO.value());

        monthlyFee.setStartDate(
                monthlyFeeDTO.startDate());

        monthlyFee.setEndDate(
                monthlyFeeDTO.endDate());

        monthlyFee.setGroup(group);

        monthlyFeeRepository.save(monthlyFee);
    }

    public List<MonthlyFeeDTO> getAllByGroupId(
            UUID groupId) {

        if (!groupUserService.isUserInGroup(
                groupId)) {

            throw new RuntimeException(
                    "User is not a member of the group");
        }

        List<MonthlyFeeEntity> monthlyFees =
                monthlyFeeRepository
                        .findByGroup_Id(groupId);

        return monthlyFees
                .stream()
                .map(monthlyFee ->
                        new MonthlyFeeDTO(
                                monthlyFee.getId(),
                                monthlyFee.getValue(),
                                monthlyFee.getGroup().getId(),
                                monthlyFee.getStartDate(),
                                monthlyFee.getEndDate()
                        ))
                .toList();
    }
}
