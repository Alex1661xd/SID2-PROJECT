package com.trackademic.model.mongo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "evaluation_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationPlan {
    @Id
    private String id;

    private String groupId; 
    private String title;

    private List<Activities> activities;
    private List<Comment> comments;
    private double totalPercentage;
    
    private String createdByStudentId;
}
