package com.trackademic.service.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackademic.model.mongo.EvaluationPlan;
import com.trackademic.repository.mongo.EvaluationPlanRepository;

@Service
public class EvaluationPlanService {

    @Autowired
    private EvaluationPlanRepository planRepository;

    // Crea o actualiza plan
    public EvaluationPlan savePlan(EvaluationPlan plan) {
        if (!isPlanValid(plan)) {
            throw new IllegalArgumentException("La suma de porcentajes debe ser 100%");
        }
        return planRepository.save(plan);
    }

    // Obtiene todos los planes
    public List<EvaluationPlan> getAllPlans() {
        List<EvaluationPlan> plans = planRepository.findAll();
        for (EvaluationPlan plan : plans) {
            if (plan.getActivities() == null) {
                plan.setActivities(new ArrayList<>());
            }
        }

        return plans;
    }

    // Busca por el id
    public Optional<EvaluationPlan> getPlanById(String id) {
        return planRepository.findById(id);
    }

    public List<EvaluationPlan> getPlansByGroupIds(List<String> groupIds) {
        return planRepository.findByGroupIdIn(groupIds);
    }


    // Busca por grupo
    public List<EvaluationPlan> getPlansByGroupId(String groupId) {
        return planRepository.findByGroupId(groupId);
    }

    // Busca por estudiante que creo el plan
    public List<EvaluationPlan> getPlansByStudentId(String studentId) {
        return planRepository.findByCreatedByStudentId(studentId);
    }

    // Elimina un plan
    public void deletePlan(String id) {
        planRepository.deleteById(id);
    }

    // Valida que la suma de porcentajes sea 100%
    public boolean isPlanValid(EvaluationPlan plan) {
        double sum = plan.getActivities()
                .stream()
                .mapToDouble(activity -> activity.getPercentage() == null ? 0.0 : activity.getPercentage())
                .sum();
        return Math.abs(sum - 100.0) < 0.001;
    }
}
