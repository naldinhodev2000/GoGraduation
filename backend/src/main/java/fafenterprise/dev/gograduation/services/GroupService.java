package fafenterprise.dev.gograduation.services;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.dto.request.GroupRequestDTO;
import fafenterprise.dev.gograduation.dto.response.GroupResponseDTO;
import fafenterprise.dev.gograduation.entity.uno.GroupEntity;
import fafenterprise.dev.gograduation.repository.GroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

    final GroupRepository groupRepository;
    final GroupUserService groupUserService;

    public GroupResponseDTO create(GroupRequestDTO groupRequestDTO) {

        GroupEntity groupEntity = new GroupEntity();

        groupEntity.setName(groupRequestDTO.name());
        groupEntity.setCourse(groupRequestDTO.course());
        groupEntity.setGoal(groupRequestDTO.goal());
        groupEntity.setTeam(groupRequestDTO.team());
        groupEntity.setToken(RandomStringUtils.secure().nextAlphanumeric(7));
        groupEntity.setCreatedAt(LocalDateTime.now());
        groupEntity.setUpdatedAt(LocalDateTime.now());

        GroupEntity newGroup = groupRepository.save(groupEntity);

        groupUserService.create(newGroup.getId());



        return new GroupResponseDTO(
            newGroup.getId(), newGroup.getName(), newGroup.getGoal(), newGroup.getTeam(), newGroup.getToken()
        );

    }

    public List<GroupResponseDTO> listAll() {
        return groupRepository.findAll()
                .stream()
                .map(group -> new GroupResponseDTO(
                        group.getId(),
                        group.getName(),
                        group.getGoal(),
                        group.getTeam(),
                        group.getToken()
                )).toList();
    }

    public GroupResponseDTO update(UUID id, GroupRequestDTO group){
        GroupEntity grp = groupRepository.findById(id).orElseThrow();

        grp.setName(group.name());
        grp.setCourse(group.course());
        grp.setGoal(group.goal());
        grp.setCourse(group.course());

        
        GroupEntity updatedGroup = groupRepository.save(grp);

        return new GroupResponseDTO(
            updatedGroup.getId(), 
            updatedGroup.getName(), 
            updatedGroup.getGoal(), 
            updatedGroup.getTeam(),
            updatedGroup.getToken()
        );
    }

    public void delete(UUID id){
        GroupEntity group = groupRepository.findById(id).orElseThrow();
        groupRepository.delete(group);
    }


    


}
