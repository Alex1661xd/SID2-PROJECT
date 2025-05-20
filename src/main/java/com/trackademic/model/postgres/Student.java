package com.trackademic.model.postgres;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")

public class Student implements UserDetails {
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

        // Implement UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Add roles if needed
    }

    @Override
    public String getUsername() {
        return email; // Use email as the username for login
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    
}
