package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "FACULTIES")
public class Faculty {
    @Id
    private Integer code;

    private String name;
    private String location;
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "dean_id")
    private Employee dean;
    
    @Transient
    private String employeeId;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Employee getDean() {
        return dean;
    }

    public void setDean(Employee dean) {
        this.dean = dean;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    
}
