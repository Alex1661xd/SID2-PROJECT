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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public City getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(City birthPlace) {
        this.birthPlace = birthPlace;
    }

    public List<GroupEnrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<GroupEnrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<StudentProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(List<StudentProgram> programs) {
        this.programs = programs;
    }

    
}
