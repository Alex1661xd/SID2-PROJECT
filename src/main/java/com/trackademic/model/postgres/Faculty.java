package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "FACULTIES")
public class Faculty {
    @Id
    private Integer code;

    private String name;
    private String location;
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "dean_id")
    private Employee dean;
}
