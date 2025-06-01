package com.trackademic.controller.mongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trackademic.model.mongo.Activities;
import com.trackademic.model.mongo.EvaluationPlan;
import com.trackademic.service.mongo.EvaluationPlanService;
import com.trackademic.service.postgres.GroupService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/evaluation-plans")
@RequiredArgsConstructor
public class EvaluationPlanControllerGrades {

    private final EvaluationPlanService planService;
    private final GroupService groupService;

    // Mostrar los planes para seleccionar el plan al cual añadir notas
    @GetMapping("/select-plan")
    public String showPlansForNotes(Model model) {
        try {
            // Obtener el email del estudiante
            String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            // Obtener los planes creados por el estudiante
            List<EvaluationPlan> userPlans = planService.getPlansByStudentId(studentEmail);
            model.addAttribute("userPlans", userPlans); // Lista de planes

            if (userPlans.isEmpty()) {
                model.addAttribute("errorMessage", "No tienes planes creados para añadir notas.");
                return "StudentHome/selectPlanForNotes"; // Vista donde el estudiante puede seleccionar un plan
            }

            return "StudentHome/selectPlanForNotes";  // Vista para seleccionar un plan
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar los planes: " + e.getMessage());
            return "StudentHome/selectPlanForNotes"; // Vista con error
        }
    }

    // Procesar la selección del plan y redirigir al formulario de añadir notas
    @PostMapping("/select-plan")
    public String selectPlanForNotes(@RequestParam String planId, Model model) {
        try {
            // Obtener el plan seleccionado por el estudiante
            EvaluationPlan plan = planService.getPlanById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));

            // Verificar que el estudiante sea el creador del plan
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!currentUser.equals(plan.getCreatedByStudentId())) {
                throw new IllegalArgumentException("No tienes permisos para añadir notas a este plan");
            }

            // Mostrar el formulario para añadir notas
            model.addAttribute("plan", plan);
            return "StudentHome/addNotesToPlans";  // Vista para añadir notas

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar el plan seleccionado: " + e.getMessage());
            return "StudentHome/selectPlanForNotes"; // Volver a la selección de planes en caso de error
        }
    }

    // Mostrar el formulario para añadir notas a las actividades del plan
    @GetMapping("/add-notes/{planId}")
    public String showAddNotesForm(@PathVariable String planId, Model model) {
        try {
            // Obtener el plan de evaluación por su id
            EvaluationPlan plan = planService.getPlanById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));

            // Verificar que el estudiante sea el creador del plan
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!currentUser.equals(plan.getCreatedByStudentId())) {
                throw new IllegalArgumentException("No tienes permisos para añadir notas a este plan");
            }

            // Añadir el plan a la vista para mostrar sus actividades
            model.addAttribute("plan", plan);
            return "StudentHome/addNotesToPlans";  // Vista donde el usuario puede añadir notas

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar el plan de evaluación: " + e.getMessage());
            return "redirect:/evaluation-plans"; // Redirige si hay un error
        }
    }

    @PostMapping("/add-notes/{planId}")
    public String addNotesToPlan(@PathVariable String planId, 
                            @RequestParam Map<String, String> allParams,
                            Model model) {
        try {
            
            // Obtener el plan seleccionado por el estudiante
            EvaluationPlan plan = planService.getPlanById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
            
            // Verificar que el estudiante sea el creador del plan
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!currentUser.equals(plan.getCreatedByStudentId())) {
                throw new IllegalArgumentException("No tienes permisos para añadir notas a este plan");
            }

            // Filtrar solo los parámetros de calificaciones (excluir _csrf y otros)
            Map<Integer, String> grades = new HashMap<>();
            
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                // Solo procesar parámetros que empiecen con "grades["
                if (key.startsWith("grades[") && key.endsWith("]")) {
                    try {
                        // Extraer el índice
                        String indexStr = key.substring(7, key.length() - 1); // grades[0] -> 0
                        int index = Integer.parseInt(indexStr);
                        
                        // Solo añadir si el valor no está vacío
                        if (value != null && !value.trim().isEmpty()) {
                            grades.put(index, value.trim());
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar índices inválidos
                    }
                }
            }
            
            // Asignar calificaciones a las actividades por índice
            List<Activities> activities = plan.getActivities();
            for (Map.Entry<Integer, String> entry : grades.entrySet()) {
                int index = entry.getKey();
                String gradeStr = entry.getValue();
                
                // Verificar que el índice sea válido
                if (index >= 0 && index < activities.size()) {
                    
                    if (!gradeStr.matches("\\d+(\\.\\d+)?")) {
                        throw new IllegalArgumentException("Valor de calificación inválido: " + gradeStr);
                    }
                    
                    Double grade = Double.parseDouble(gradeStr);
                    
                    // Validar la calificación
                    if (grade < 0 || grade > 5.0) {
                        throw new IllegalArgumentException("Las notas deben estar entre 0 y 5.0");
                    }
                    
                    activities.get(index).setGrade(grade);
                }
            }

            // Guardar el plan actualizado con las notas
            planService.savePlan(plan);

            model.addAttribute("successMessage", "Notas guardadas exitosamente.");
            model.addAttribute("plan", plan); // Recargar el plan actualizado
            return "StudentHome/addNotesToPlans";

        } catch (Exception e) {
            System.out.println("ERROR en addNotesToPlan: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al añadir las notas: " + e.getMessage());
            model.addAttribute("plan", planService.getPlanById(planId).orElse(null));
            return "StudentHome/addNotesToPlans";
        }
    }

    // Nueva funcionalidad: Eliminar nota de una actividad específica
    @PostMapping("/remove-grade/{planId}/{activityIndex}")
    public String removeGradeFromActivity(@PathVariable String planId, 
                                        @PathVariable int activityIndex,
                                        Model model) {
        try {
            // Obtener el plan
            EvaluationPlan plan = planService.getPlanById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
            
            // Verificar que el estudiante sea el creador del plan
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!currentUser.equals(plan.getCreatedByStudentId())) {
                throw new IllegalArgumentException("No tienes permisos para modificar este plan");
            }

            // Verificar que el índice sea válido
            if (activityIndex < 0 || activityIndex >= plan.getActivities().size()) {
                throw new IllegalArgumentException("Índice de actividad inválido");
            }

            // Eliminar la calificación (establecer como null)
            plan.getActivities().get(activityIndex).setGrade(null);

            // Guardar el plan actualizado
            planService.savePlan(plan);

            model.addAttribute("successMessage", "Nota eliminada exitosamente.");
            model.addAttribute("plan", plan);
            return "StudentHome/addNotesToPlans";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al eliminar la nota: " + e.getMessage());
            model.addAttribute("plan", planService.getPlanById(planId).orElse(null));
            return "StudentHome/addNotesToPlans";
        }
    }

    // Nueva funcionalidad: Actualizar nota de una actividad específica
    @PostMapping("/update-grade/{planId}/{activityIndex}")
    public String updateSingleGrade(@PathVariable String planId, 
                                  @PathVariable int activityIndex,
                                  @RequestParam String grade,
                                  Model model) {
        try {
            // Obtener el plan
            EvaluationPlan plan = planService.getPlanById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
            
            // Verificar que el estudiante sea el creador del plan
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!currentUser.equals(plan.getCreatedByStudentId())) {
                throw new IllegalArgumentException("No tienes permisos para modificar este plan");
            }

            // Verificar que el índice sea válido
            if (activityIndex < 0 || activityIndex >= plan.getActivities().size()) {
                throw new IllegalArgumentException("Índice de actividad inválido");
            }

            // Validar y asignar la calificación si no está vacía
            if (grade != null && !grade.trim().isEmpty()) {
                if (!grade.matches("\\d+(\\.\\d+)?")) {
                    throw new IllegalArgumentException("Valor de calificación inválido: " + grade);
                }
                
                Double gradeValue = Double.parseDouble(grade);
                
                if (gradeValue < 0 || gradeValue > 5.0) {
                    throw new IllegalArgumentException("Las notas deben estar entre 0 y 5.0");
                }
                
                plan.getActivities().get(activityIndex).setGrade(gradeValue);
            } else {
                // Si está vacío, eliminar la calificación
                plan.getActivities().get(activityIndex).setGrade(null);
            }

            // Guardar el plan actualizado
            planService.savePlan(plan);

            model.addAttribute("successMessage", "Nota actualizada exitosamente.");
            model.addAttribute("plan", plan);
            return "StudentHome/addNotesToPlans";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al actualizar la nota: " + e.getMessage());
            model.addAttribute("plan", planService.getPlanById(planId).orElse(null));
            return "StudentHome/addNotesToPlans";
        }
    }
}