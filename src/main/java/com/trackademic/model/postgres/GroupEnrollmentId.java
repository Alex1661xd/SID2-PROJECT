package com.trackademic.model.postgres;

import java.io.Serializable;
import java.util.Objects;

public class GroupEnrollmentId implements Serializable {
    private Integer groupNumber;
    private String groupSubjectCode;
    private String groupSemester;
    private Long student;

    // Getters and setters
    public Integer getGroupNumber() { return groupNumber; }
    public void setGroupNumber(Integer groupNumber) { this.groupNumber = groupNumber; }

    public String getGroupSubjectCode() { return groupSubjectCode; }
    public void setGroupSubjectCode(String groupSubjectCode) { this.groupSubjectCode = groupSubjectCode; }

    public String getGroupSemester() { return groupSemester; }
    public void setGroupSemester(String groupSemester) { this.groupSemester = groupSemester; }

    public Long getStudent() { return student; }
    public void setStudent(Long student) { this.student = student; }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupEnrollmentId)) return false;
        GroupEnrollmentId that = (GroupEnrollmentId) o;
        return Objects.equals(groupNumber, that.groupNumber) &&
               Objects.equals(groupSubjectCode, that.groupSubjectCode) &&
               Objects.equals(groupSemester, that.groupSemester) &&
               Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupNumber, groupSubjectCode, groupSemester, student);
    }
}
