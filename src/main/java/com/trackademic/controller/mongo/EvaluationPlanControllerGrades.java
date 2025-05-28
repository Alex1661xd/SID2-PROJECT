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
                        grades.put(index, value);
                    } catch (NumberFormatException e) {
                
                    }
                }
            }
            
            // Verificar que tenemos el número correcto de calificaciones
            if (grades.size() != plan.getActivities().size()) {
                throw new IllegalArgumentException("Número de calificaciones no coincide con número de actividades");
            }
            
            // Asignar calificaciones a las actividades por índice
            List<Activities> activities = plan.getActivities();
            for (int i = 0; i < activities.size(); i++) {
                if (grades.containsKey(i)) {
                    String gradeStr = grades.get(i);
                    
                    if (!gradeStr.matches("\\d+(\\.\\d+)?")) {
                        throw new IllegalArgumentException("Valor de calificación inválido: " + gradeStr);
                    }
                    
                    Double grade = Double.parseDouble(gradeStr);
                    
                    // Validar la calificación
                    if (grade < 0 || grade > 5.0) {
                        throw new IllegalArgumentException("Las notas deben estar entre 0 y 5.0");
                    }
                    
                    activities.get(i).setGrade(grade);
                }
            }

            // Guardar el plan actualizado con las notas
            planService.savePlan(plan);

            model.addAttribute("successMessage", "Notas añadidas exitosamente.");
            return "redirect:/evaluation-plans/my-plans";

        } catch (Exception e) {
            System.out.println("ERROR en addNotesToPlan: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al añadir las notas: " + e.getMessage());
            model.addAttribute("plan", planService.getPlanById(planId).orElse(null));
            return "StudentHome/addNotesToPlans";
        }
    }

}
