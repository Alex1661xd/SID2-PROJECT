package com.trackademic.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "evaluationPlans")
public class EvaluationPlan {

    @Id
    private String id;
    private String courseName;
    private String semester;

    // Constructores
    public EvaluationPlan() {}

    public EvaluationPlan(String courseName, String semester) {
        this.courseName = courseName;
        this.semester = semester;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}
