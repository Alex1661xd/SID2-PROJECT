package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "SUBJECTS")
public class Subject {
    @Id
    private String code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "program_code", nullable = false)
    private Program program;
}
