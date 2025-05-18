package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Integer> {
}
