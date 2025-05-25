package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.GroupEnrollment;
import com.trackademic.model.postgres.GroupEnrollmentId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupEnrollmentRepository extends JpaRepository<GroupEnrollment, GroupEnrollmentId> {
     List<GroupEnrollment> findByStudent(Long studentId);
}
