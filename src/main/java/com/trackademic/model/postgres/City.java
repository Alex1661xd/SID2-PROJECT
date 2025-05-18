package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "CITIES")
public class City {
    @Id
    private Integer code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "dept_code", nullable = false)
    private Department department;

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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    
}
