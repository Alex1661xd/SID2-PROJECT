package com.trackademic.repository.mongo;

import com.trackademic.model.mongo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    // all comments for an eval plan
    List<Comment> findByPlanId(String planId);
    List<Comment> findByStudentId(Long studentId);
}
