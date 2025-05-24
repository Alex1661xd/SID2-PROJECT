package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "GROUPS")
@IdClass(GroupId.class)
public class Group {
    @Id
    private Integer number;

    @Id
    private String subjectCode;

    @Id
    private String semester;
    
    @Transient
    private String professorId;
    

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Employee professor;

    public Employee getProfessor() {
        return professor;
    }

    public void setProfessor(Employee professor) {
        this.professor = professor;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

}
