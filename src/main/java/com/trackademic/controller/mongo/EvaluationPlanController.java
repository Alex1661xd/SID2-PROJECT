package com.trackademic.controller.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trackademic.model.mongo.EvaluationPlan;
import com.trackademic.model.postgres.GroupEnrollment;
import com.trackademic.service.mongo.EvaluationPlanService;
import com.trackademic.service.postgres.GroupService;

@Controller
@RequestMapping("/evaluation-plans")
public class EvaluationPlanController {

    @Autowired
    private EvaluationPlanService planService;
    @Autowired
    private GroupService groupService;

    @GetMapping
    public String listPlans(Model model) {
        List<EvaluationPlan> plans = planService.getAllPlans();
       
        for (EvaluationPlan plan : plans) {
            double totalPercentage = plan.getActivities().stream()
                .mapToDouble(activity -> activity.getPercentage() != null ? activity.getPercentage() : 0.0)
                .sum();
            
            model.addAttribute("totalPercentage_" + plan.getId(), String.format("%.1f", totalPercentage));
        }

        model.addAttribute("plans", plans);
        return "StudentHome/viewEvaluationPlans";
    }

    @GetMapping("/group/{groupId}")
    public String getPlansByGroup(@PathVariable String groupId, Model model) {
        List<EvaluationPlan> plans = planService.getPlansByGroupId(groupId);
        model.addAttribute("plans", plans);
        return "StudentHome/viewEvaluationPlansGroups";  
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        
        List<GroupEnrollment> studentGroups = groupService.getEnrollmentsByStudentEmail(studentEmail);
        
        model.addAttribute("plan", new EvaluationPlan());
        model.addAttribute("studentGroups", studentGroups);
        return "StudentHome/createEvaluationPlans";
    }

    @PostMapping
    public String savePlan(@ModelAttribute EvaluationPlan plan, Model model) {
        try {
            // Obtener el email del usuario autenticado para asignar como creador
            String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            plan.setCreatedByStudentId(studentEmail);
            
            if (!planService.isPlanValid(plan)) {
                model.addAttribute("errorMessage", "La suma de los porcentajes debe ser exactamente 100%");
                
                // Recargar los grupos del estudiante para mostrar el formulario nuevamente
                List<GroupEnrollment> studentGroups = groupService.getEnrollmentsByStudentEmail(studentEmail);
                model.addAttribute("studentGroups", studentGroups);
                model.addAttribute("plan", plan);
                return "StudentHome/createEvaluationPlans";
            }
            
            planService.savePlan(plan);
            return "redirect:/evaluation-plans/my-plans";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            
            // Recargar los grupos del estudiante
            String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<GroupEnrollment> studentGroups = groupService.getEnrollmentsByStudentEmail(studentEmail);
            model.addAttribute("studentGroups", studentGroups);
            model.addAttribute("plan", plan);
            return "StudentHome/createEvaluationPlans";
        }
    }

    @GetMapping("/my-plans")
    public String listMyPlans(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<EvaluationPlan> userPlans = planService.getPlansByStudentId(username); 
        model.addAttribute("userPlans", userPlans);
        return "StudentHome/myEvaluationPlans";  
    }

    @GetMapping("/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        EvaluationPlan plan = planService.getPlanById(id).orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
        model.addAttribute("plan", plan);
        return "StudentHome/editEvaluationPlan";  
    }

    @PostMapping("/{id}")
    public String updatePlan(@PathVariable String id, @ModelAttribute EvaluationPlan plan) {
        plan.setId(id);  // Aseguramos que el ID no se pierda
        planService.savePlan(plan);
        return "redirect:/evaluation-plans";
    }

    @DeleteMapping("/{id}")
    public String deletePlan(@PathVariable String id) {
        planService.deletePlan(id);
        return "redirect:/evaluation-plans";
    }
}
