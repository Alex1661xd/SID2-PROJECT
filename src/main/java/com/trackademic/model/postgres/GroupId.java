package com.trackademic.model.postgres;

import java.io.Serializable;
import java.util.Objects;

public class GroupId implements Serializable {
    private Integer number;
    private String subjectCode;
    private String semester;
    
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public String getSubjectCode() {
        return subjectCode;
    }
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
    public String getSemester() {
        return semester;
    }
    public void setSemester(String semester) {
        this.semester = semester;
    }

    // equals() and hashCode() implementations
    
}
