package com.trackademic.repository.mongo;

import com.trackademic.model.mongo.EvaluationPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EvaluationPlanRepository extends MongoRepository<EvaluationPlan, String> {
}
