package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
