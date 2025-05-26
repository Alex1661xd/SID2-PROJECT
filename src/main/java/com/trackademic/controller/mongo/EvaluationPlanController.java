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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        try {
            String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<GroupEnrollment> enrollments = groupService.getEnrollmentsByStudentEmail(studentEmail);

            List<String> groupIds = enrollments.stream()
                .map(enrollment -> enrollment.getGroup().getNumber() + "-" +
                                enrollment.getGroup().getSubjectCode() + "-" +
                                enrollment.getGroup().getSemester())
                .toList();

            List<EvaluationPlan> plans = planService.getPlansByGroupIds(groupIds);
            System.out.println("Group IDs del estudiante: " + groupIds);

            // 👇 este es el atributo que necesitas para el <select>
            model.addAttribute("studentGroups", enrollments);
            model.addAttribute("plans", plans);
            model.addAttribute("currentUser", studentEmail);

            return "StudentHome/viewEvaluationPlans";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar los planes: " + e.getMessage());
            model.addAttribute("plans", List.of());
            model.addAttribute("studentGroups", List.of()); // Agrega también en error
            return "StudentHome/viewEvaluationPlans";
        }
    }


    @GetMapping("/group/{groupId}")
    public String getPlansByGroup(@PathVariable String groupId, Model model) {
        try {
            List<EvaluationPlan> plans = planService.getPlansByGroupId(groupId);
            model.addAttribute("plans", plans);
            model.addAttribute("groupId", groupId);
            
            System.out.println("Planes encontrados para grupo " + groupId + ": " + plans.size());
            return "StudentHome/viewEvaluationPlansGroups";
        } catch (Exception e) {
            System.err.println("Error al cargar planes del grupo " + groupId + ": " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al cargar los planes del grupo: " + e.getMessage());
            model.addAttribute("plans", List.of());
            model.addAttribute("groupId", groupId);
            return "StudentHome/viewEvaluationPlansGroups";
        }
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
            String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            plan.setCreatedByStudentId(studentEmail);
            
            if (!planService.isPlanValid(plan)) {
                model.addAttribute("errorMessage", "La suma de los porcentajes debe ser exactamente 100%");
                
                List<GroupEnrollment> studentGroups = groupService.getEnrollmentsByStudentEmail(studentEmail);
                model.addAttribute("studentGroups", studentGroups);
                model.addAttribute("plan", plan);
                return "StudentHome/createEvaluationPlans";
            }
            
            planService.savePlan(plan);
            return "redirect:/evaluation-plans/my-plans";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            
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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        try {
            EvaluationPlan plan = planService.getPlanById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
            
            // Verificar que el usuario sea el creador del plan
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!currentUser.equals(plan.getCreatedByStudentId())) {
                throw new IllegalArgumentException("No tienes permisos para editar este plan");
            }
            
            String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<GroupEnrollment> studentGroups = groupService.getEnrollmentsByStudentEmail(studentEmail);
            
            model.addAttribute("plan", plan);
            model.addAttribute("studentGroups", studentGroups);
            model.addAttribute("isEditing", true);
            
            System.out.println("Editando plan: " + plan.getId() + " - " + plan.getTitle());
            return "StudentHome/editEvaluationPlan";
        } catch (Exception e) {
            System.err.println("Error al cargar plan para edición: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al cargar el plan: " + e.getMessage());
            return "redirect:/evaluation-plans/my-plans";
        }
    }

    @PutMapping("/edit/{id}")
    public String updatePlan(@PathVariable String id, @ModelAttribute EvaluationPlan plan, 
                           Model model, RedirectAttributes redirectAttributes) {
        try {
            // Verificar que el plan existe y pertenece al usuario
            EvaluationPlan existingPlan = planService.getPlanById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
            
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!currentUser.equals(existingPlan.getCreatedByStudentId())) {
                throw new IllegalArgumentException("No tienes permisos para editar este plan");
            }
            
            // Mantener los datos importantes del plan original
            plan.setId(id);
            plan.setCreatedByStudentId(existingPlan.getCreatedByStudentId());
            
            if (!planService.isPlanValid(plan)) {
                model.addAttribute("errorMessage", "La suma de los porcentajes debe ser exactamente 100%");
                List<GroupEnrollment> studentGroups = groupService.getEnrollmentsByStudentEmail(currentUser);
                model.addAttribute("studentGroups", studentGroups);
                model.addAttribute("plan", plan);
                model.addAttribute("isEditing", true);
                return "StudentHome/editEvaluationPlan";
            }
            
            planService.savePlan(plan);
            System.out.println("Plan actualizado exitosamente: " + id);
            
            redirectAttributes.addFlashAttribute("successMessage", "Plan actualizado exitosamente");
            return "redirect:/evaluation-plans/my-plans";
            
        } catch (Exception e) {
            System.err.println("Error al actualizar plan: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al actualizar el plan: " + e.getMessage());
            
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            List<GroupEnrollment> studentGroups = groupService.getEnrollmentsByStudentEmail(currentUser);
            model.addAttribute("studentGroups", studentGroups);
            model.addAttribute("plan", plan);
            model.addAttribute("isEditing", true);
            return "StudentHome/editEvaluationPlan";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deletePlan(@PathVariable String id) {
        try {
            planService.deletePlan(id);  // Eliminar el plan por su ID
        } catch (Exception e) {
            // Si hay un error al eliminar, mostrar un mensaje adecuado
            return "redirect:/evaluation-plans/my-plans?error=true";
        }
        return "redirect:/evaluation-plans/my-plans";  // Redirige a la lista de planes después de eliminar
    }


}
