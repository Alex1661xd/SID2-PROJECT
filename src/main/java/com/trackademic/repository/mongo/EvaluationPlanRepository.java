package com.trackademic.repository.mongo;

import com.trackademic.model.mongo.EvaluationPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface EvaluationPlanRepository extends MongoRepository<EvaluationPlan, String> {
    List<EvaluationPlan> findByGroupId(String groupId);
    List<EvaluationPlan> findByCreatedByStudentId(String studentId);
}
