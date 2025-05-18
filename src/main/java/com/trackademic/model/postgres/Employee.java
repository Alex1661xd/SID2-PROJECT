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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public City getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(City birthPlace) {
        this.birthPlace = birthPlace;
    }

    
}
