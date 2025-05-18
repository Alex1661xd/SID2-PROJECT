package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<Campus, Integer> {
}
