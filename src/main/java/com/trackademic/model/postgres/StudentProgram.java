package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "student_programs")

@IdClass(StudentProgramId.class)
public class StudentProgram {

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Id
    @ManyToOne
    @JoinColumn(name = "program_code")
    private Program program;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    
}
