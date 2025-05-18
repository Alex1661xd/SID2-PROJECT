package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "PROGRAMS")
public class Program {
    @Id
    private Integer code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "area_code", nullable = false)
    private Area area;
}
