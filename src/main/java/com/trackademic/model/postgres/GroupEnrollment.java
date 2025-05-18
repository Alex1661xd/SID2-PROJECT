package com.trackademic.model.postgres;
import jakarta.persistence.*;
@Entity
@Table(name = "group_enrollments")
@IdClass(GroupEnrollmentId.class)
public class GroupEnrollment {

    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "group_number", referencedColumnName = "number"),
        @JoinColumn(name = "group_subject_code", referencedColumnName = "subjectCode"),
        @JoinColumn(name = "group_semester", referencedColumnName = "semester")
    })
    private Group group;

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Campos adicionales opcionales
    private Double finalGrade;
    private String status; // "Activo", "Retirado", etc.
}
