package com.trackademic.model.postgres;

import java.io.Serializable;
import java.util.Objects;

public class StudentProgramId implements Serializable {
    private Long student;
    private Integer program;
    
    public Long getStudent() {
        return student;
    }
    public void setStudent(Long student) {
        this.student = student;
    }
    public Integer getProgram() {
        return program;
    }
    public void setProgram(Integer program) {
        this.program = program;
    }

    // equals() y hashCode()
}
