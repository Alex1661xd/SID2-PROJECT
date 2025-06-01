package com.trackademic.controller.mongo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.trackademic.model.mongo.Comment;
import com.trackademic.model.postgres.Student;
import com.trackademic.repository.postgres.StudentRepository;
import com.trackademic.service.mongo.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trackademic.model.mongo.EvaluationPlan;
import com.trackademic.model.postgres.GroupEnrollment;
import com.trackademic.service.mongo.EvaluationPlanService;
import com.trackademic.service.postgres.GroupService;
import jakarta.validation.ValidationException;

@Controller
@RequestMapping("/evaluation-plans")
@RequiredArgsConstructor
public class EvaluationPlanController {

    @Autowired
    private EvaluationPlanService planService;
    @Autowired
    private GroupService groupService;
    private final CommentService commentService;
    private final StudentRepository studentRepository;

    
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
            model.addAttribute("currentUser", SecurityContextHolder.getContext().getAuthentication().getName()); // ADD THIS
            System.out.println("Planes encontrados para grupo " + groupId + ": " + plans.size());
            return "StudentHome/viewEvaluationPlansGroups";
        } catch (Exception e) {
            System.err.println("Error al cargar planes del grupo " + groupId + ": " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al cargar los planes del grupo: " + e.getMessage());
            model.addAttribute("plans", List.of());
            model.addAttribute("groupId", groupId);
            model.addAttribute("currentUser", SecurityContextHolder.getContext().getAuthentication().getName()); // ADD THIS
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

    @GetMapping("/{id}")
    public String viewPlanDetail(@PathVariable String id, Model model) {
        EvaluationPlan plan = planService.getPlanById(id)
            .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
        model.addAttribute("plan", plan);
        return "StudentHome/viewPlanDetail";
    }

    // NEW COMMENT ENDPOINTS
    @GetMapping("/{id}/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPlanComments(@PathVariable String id) {
        List<Comment> comments = commentService.getCommentsByPlanId(id);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments);
        response.put("currentStudentId", student.getId());
        response.put("name", student.getFirstName());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/comments")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addComment(@PathVariable String id, @RequestParam String content) {
        try {
            commentService.addComment(id, content);
            return ResponseEntity.ok(Map.of("message", "Comentario añadido exitosamente"));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/comments/{commentId}/edit")
    @ResponseBody
    public ResponseEntity<Map<String, String>> editComment(@PathVariable String commentId, @RequestParam String content) {
        try {
            commentService.editComment(commentId, content);
            return ResponseEntity.ok(Map.of("message", "Comentario actualizado exitosamente"));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/comments/{commentId}/delete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable String commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok(Map.of("message", "Comentario eliminado exitosamente"));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }
}
