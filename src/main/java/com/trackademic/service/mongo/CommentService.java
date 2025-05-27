package com.trackademic.service.mongo;

import com.trackademic.model.mongo.Comment;
import com.trackademic.model.mongo.EvaluationPlan;
import com.trackademic.model.postgres.Student;
import com.trackademic.repository.mongo.CommentRepository;
import com.trackademic.repository.mongo.EvaluationPlanRepository;
import com.trackademic.repository.postgres.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EvaluationPlanRepository evaluationPlanRepository;
    private final StudentRepository studentRepository;

    public void addComment(String planId, String content){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow(() -> new ValidationException("Student not found: " + email));

        Optional<EvaluationPlan> planOpt = evaluationPlanRepository.findById(planId);
        if (planOpt.isEmpty()){
            throw new ValidationException("Plan not found: " + planId);
        }
        if (content == null || content.trim().isEmpty()){
            throw new ValidationException("Content cannot be empty");
        }
        if (content.length() > 1000){
            throw new ValidationException("Content cannot be longer than 1000 characters");
        }

        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .planId(planId)
                .studentId(student.getId())
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    public void editComment(String commentId, String newContent){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow(() -> new ValidationException("Student not found: " + email));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ValidationException("Comment not found: " + commentId));

        if (!comment.getStudentId().equals(student.getId())){
            throw new ValidationException("You can only edit your own comments");
        }
        if (newContent == null || newContent.trim().isEmpty()){
            throw new ValidationException("Content cannot be empty");
        }
        if (newContent.length() > 1000){
            throw new ValidationException("Content cannot be longer than 1000 characters");
        }
        comment.setContent(newContent);
        comment.setTimestamp(LocalDateTime.now());
        commentRepository.save(comment);
    }
    public void deleteComment(String commentId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow(() -> new ValidationException("Student not found: " + email));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ValidationException("Comment not found: " + commentId));

        if (!comment.getStudentId().equals(student.getId())){
            throw new ValidationException("You can only delete your own comments");
        }
        commentRepository.delete(comment);
    }
    public List<Comment> getCommentsByPlanId(String planId){
        return commentRepository.findByPlanId(planId);
    }
}
