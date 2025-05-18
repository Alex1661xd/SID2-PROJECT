package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEES")
public class Employee {
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;

    @ManyToOne
    @JoinColumn(name = "contract_type", nullable = false)
    private ContractType contractType;

    @ManyToOne
    @JoinColumn(name = "employee_type", nullable = false)
    private EmployeeType employeeType;

    @ManyToOne
    @JoinColumn(name = "faculty_code", nullable = false)
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "campus_code", nullable = false)
    private Campus campus;

    @ManyToOne
    @JoinColumn(name = "birth_place_code", nullable = false)
    private City birthPlace;
}
