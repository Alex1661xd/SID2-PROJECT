package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, String> {
}
