package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "DEPARTMENTS")
public class Department {
    @Id
    private Integer code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "country_code", nullable = false)
    private Country country;

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    
}
