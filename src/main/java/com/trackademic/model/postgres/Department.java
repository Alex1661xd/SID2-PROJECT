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
}
