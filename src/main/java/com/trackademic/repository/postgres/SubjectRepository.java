package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, String> {
}
