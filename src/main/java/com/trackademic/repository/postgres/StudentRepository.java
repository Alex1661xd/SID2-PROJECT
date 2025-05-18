package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
