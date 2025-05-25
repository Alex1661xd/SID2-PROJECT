package com.trackademic.model.postgres;

import java.io.Serializable;
import java.util.Objects;

public class GroupId implements Serializable {

    private Integer number;
    private String subjectCode;
    private String semester;

    // Constructor vacío obligatorio para Hibernate
    public GroupId() {}

    public GroupId(Integer number, String subjectCode, String semester) {
        this.number = number;
        this.subjectCode = subjectCode;
        this.semester = semester;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupId)) return false;
        GroupId groupId = (GroupId) o;
        return Objects.equals(number, groupId.number) &&
               Objects.equals(subjectCode, groupId.subjectCode) &&
               Objects.equals(semester, groupId.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, subjectCode, semester);
    }
}
