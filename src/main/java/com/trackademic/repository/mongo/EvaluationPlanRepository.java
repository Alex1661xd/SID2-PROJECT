package com.trackademic.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trackademic.model.mongo.EvaluationPlan;

public interface EvaluationPlanRepository extends MongoRepository<EvaluationPlan, String> {
    List<EvaluationPlan> findByGroupId(String groupId);
    List<EvaluationPlan> findByCreatedByStudentId(String studentId);
    List<EvaluationPlan> findByGroupIdIn(List<String> groupIds);

}
