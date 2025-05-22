package com.trackademic.model.postgres;

import jakarta.persistence.*;

@Entity
@Table(name = "PROGRAMS")
public class Program {
    @Id
    private Integer code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "area_code", nullable = false)
    private Area area;

    @Transient
    private int areaId;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    
}
