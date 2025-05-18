package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "CAMPUSES")
public class Campus {
    @Id
    private Integer code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "city_code", nullable = false)
    private City city;

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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    
}
