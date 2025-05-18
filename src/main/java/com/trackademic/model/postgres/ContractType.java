package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "CONTRACT_TYPES")
public class ContractType {
    @Id
    private String name;
}
