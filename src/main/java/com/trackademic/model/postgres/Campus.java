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
}
