package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "COUNTRIES")
public class Country {
    @Id
    private Integer code;

    private String name;
}
