package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "GROUPS")
@IdClass(GroupId.class)
public class Group {
    @Id
    private Integer number;

    @Id
    private String subjectCode;

    @Id
    private String semester;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Employee professor;
}
