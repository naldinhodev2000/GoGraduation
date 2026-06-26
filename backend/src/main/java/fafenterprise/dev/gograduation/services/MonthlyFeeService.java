package fafenterprise.dev.gograduation.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.MonthlyFeeDTO;
import fafenterprise.dev.gograduation.entity.relationship.GroupUserEntity;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.entity.uno.MonthlyFeeEntity;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import fafenterprise.dev.gograduation.repository.GroupUserRepository;
import fafenterprise.dev.gograduation.repository.MonthlyFeeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonthlyFeeService {
    final GroupRepository groupRepository;
    final GroupUserRepository groupUserRepository;
    final MonthlyFeeRepository monthlyFeeRepository;
    final JwtService jwtService;
    final GroupUserService groupUserService;

    public void create(MonthlyFeeDTO monthlyFeeDTO) {
        

        if(!groupUserService.isUserInGroup(monthlyFeeDTO.groupId())){
            throw new RuntimeException("User is not a member of the group");
        }
        if(!groupUserService.isUserAdmin(monthlyFeeDTO.groupId())) {
            throw new RuntimeException("User is not an admin of the group");
        }


        MonthlyFeeEntity monthlyFee = new MonthlyFeeEntity();
        GroupEntity group = groupRepository.findById(monthlyFeeDTO.groupId()).orElseThrow();

        monthlyFee.setValue(monthlyFeeDTO.value());
        monthlyFee.setStartDate(monthlyFeeDTO.startDate());
        monthlyFee.setEndDate(monthlyFeeDTO.endDate());
        monthlyFee.setGroup(group);

        monthlyFeeRepository.save(monthlyFee);

    }

    public void update(UUID id, MonthlyFeeDTO monthlyFeeDTO) {

        
        if(!groupUserService.isUserInGroup(monthlyFeeDTO.groupId())){
            throw new RuntimeException("User is not a member of the group");
        }
        if(!groupUserService.isUserAdmin(monthlyFeeDTO.groupId())) {
            throw new RuntimeException("User is not an admin of the group");
        }

        MonthlyFeeEntity monthlyFee = monthlyFeeRepository.findById(id).orElseThrow();
        GroupEntity group = groupRepository.findById(monthlyFeeDTO.groupId()).orElseThrow();

        monthlyFee.setValue(monthlyFeeDTO.value());
        monthlyFee.setStartDate(monthlyFeeDTO.startDate());
        monthlyFee.setEndDate(monthlyFeeDTO.endDate());
        monthlyFee.setGroup(group);

        monthlyFeeRepository.save(monthlyFee);

    }


    List<MonthlyFeeDTO> getAllByGroupId(UUID groupId) {
        
        if(!groupUserService.isUserInGroup(groupId)){
            throw new RuntimeException("User is not a member of the group");
        }
        if(!groupUserService.isUserAdmin(groupId)) {
            throw new RuntimeException("User is not an admin of the group");
        }

        List<MonthlyFeeEntity> monthlyFees = monthlyFeeRepository.findByGroup_Id(groupId);
        return monthlyFees.stream()
                .map(monthlyFee -> {
                    return new MonthlyFeeDTO(
                            monthlyFee.getId(),
                            monthlyFee.getValue(),
                            monthlyFee.getGroup().getId(),
                            monthlyFee.getStartDate(),
                            monthlyFee.getEndDate()
                        
                    );
                })
                .toList(); 
    }

    

}
