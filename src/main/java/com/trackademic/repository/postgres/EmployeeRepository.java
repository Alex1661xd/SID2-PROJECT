package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
