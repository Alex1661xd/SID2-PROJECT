package com.trackademic.service.postgres;

import org.springframework.stereotype.Service;
import com.trackademic.model.postgres.Group;
import com.trackademic.repository.postgres.GroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {
    
    private final GroupRepository groupRepository;

    public int getIdentifierGroupById(int id) {
        Group group = groupRepository.findById(id);
        if (group != null) {
            return group.getNumber();
        } else {
            throw new IllegalArgumentException("Group not found with id: " + id);
        }
    }
}
