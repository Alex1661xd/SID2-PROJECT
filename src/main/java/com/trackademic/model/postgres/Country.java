package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "COUNTRIES")
public class Country {
    @Id
    private Integer code;

    private String name;

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

    
}
