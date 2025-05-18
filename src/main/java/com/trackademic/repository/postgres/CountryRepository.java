package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
