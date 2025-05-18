package com.trackademic.model.postgres;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "AREAS")
public class Area {
    @Id
    private Integer code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_code", nullable = false)
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "coordinator_id", nullable = false)
    private Employee coordinator;

    @OneToMany(mappedBy = "area")
    private List<Program> programs;
}
