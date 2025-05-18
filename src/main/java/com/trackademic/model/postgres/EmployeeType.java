package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEE_TYPES")
public class EmployeeType {
    @Id
    private String name;
}
