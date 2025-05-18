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
}
