package com.trackademic.repository.postgres;

import com.trackademic.model.postgres.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractTypeRepository extends JpaRepository<ContractType, String> {
}
