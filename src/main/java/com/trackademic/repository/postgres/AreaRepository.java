package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Integer> {
}
