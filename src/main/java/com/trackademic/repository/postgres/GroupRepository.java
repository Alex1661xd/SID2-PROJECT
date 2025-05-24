package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Group;
import com.trackademic.model.postgres.GroupId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, GroupId> {

    public Group findById(int id);
    
}
