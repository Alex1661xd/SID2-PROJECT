package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "group_enrollments")
@IdClass(GroupEnrollmentId.class)
public class GroupEnrollment {

    @Id
    @Column(name = "group_number")
    private Integer groupNumber;

    @Id
    @Column(name = "group_subject_code")
    private String groupSubjectCode;

    @Id
    @Column(name = "group_semester")
    private String groupSemester;

    @Id
    @Column(name = "student_id")
    private Long student;

    // Relaciones (opcional si necesitas los objetos, pero no parte del ID)
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "group_number", referencedColumnName = "number", insertable = false, updatable = false),
        @JoinColumn(name = "group_subject_code", referencedColumnName = "subjectCode", insertable = false, updatable = false),
        @JoinColumn(name = "group_semester", referencedColumnName = "semester", insertable = false, updatable = false)
    })
    private Group group;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Student studentEntity;

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getGroupSubjectCode() {
        return groupSubjectCode;
    }

    public void setGroupSubjectCode(String groupSubjectCode) {
        this.groupSubjectCode = groupSubjectCode;
    }

    public String getGroupSemester() {
        return groupSemester;
    }

    public void setGroupSemester(String groupSemester) {
        this.groupSemester = groupSemester;
    }

    public Long getStudent() {
        return student;
    }

    public void setStudent(Long student) {
        this.student = student;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Student getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(Student studentEntity) {
        this.studentEntity = studentEntity;
    }

    
}
