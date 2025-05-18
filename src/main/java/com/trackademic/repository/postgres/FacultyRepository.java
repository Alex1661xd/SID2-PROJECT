package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
}
