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

    @Transient
    private int programId;

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    
}
