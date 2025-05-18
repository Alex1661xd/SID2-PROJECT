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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Employee getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Employee coordinator) {
        this.coordinator = coordinator;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
    
}
