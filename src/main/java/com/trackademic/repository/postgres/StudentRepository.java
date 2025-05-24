package com.trackademic.repository.postgres;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trackademic.model.postgres.Student;

public interface  StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
}
