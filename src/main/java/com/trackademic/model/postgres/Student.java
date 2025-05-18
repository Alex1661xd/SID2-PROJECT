package com.trackademic.model.postgres;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "students")

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String documentNumber;

    @ManyToOne
    @JoinColumn(name = "campus_code")
    private Campus campus;

    @ManyToOne
    @JoinColumn(name = "birth_place_code")
    private City birthPlace;

    @OneToMany(mappedBy = "student")
    private List<GroupEnrollment> enrollments;

    @OneToMany(mappedBy = "student")
    private List<StudentProgram> programs;
}
