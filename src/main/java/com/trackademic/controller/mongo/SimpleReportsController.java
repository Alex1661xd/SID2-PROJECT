package com.trackademic.controller.mongo;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trackademic.model.mongo.Activities;
import com.trackademic.model.mongo.Comment;
import com.trackademic.model.mongo.EvaluationPlan;
import com.trackademic.service.mongo.EvaluationPlanService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class SimpleReportsController {

    private final EvaluationPlanService planService;

     @GetMapping
    public String showReportsMenu(Model model) {
        try {
            String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<EvaluationPlan> userPlans = planService.getPlansByStudentId(studentEmail);
            if (userPlans.isEmpty()) {
                
                model.addAttribute("errorMessage", "No tienes planes disponibles.");
                return "reports/reportsMenu";  // Redirige a una vista donde el mensaje de error se muestre
            }

            model.addAttribute("plans", userPlans); // Asegúrate de pasar la lista de planes al modelo
            System.out.println("hola 3");
            return "reports/reportsMenu";  // Vista de menú de reportes
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar el menú de informes: " + e.getMessage());
            return "reports/reportsMenu";  // Redirige a la misma vista si hay un error
        }
    }
    // Informe de notas por plan
    @GetMapping("/plan/{planId}")
    public String planReport(@PathVariable String planId, Model model) {
        try {
            String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            EvaluationPlan plan = planService.getPlanById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
            
            // Verificar permisos
            if (!studentEmail.equals(plan.getCreatedByStudentId())) {
                throw new IllegalArgumentException("No tienes permisos para ver este plan");
            }
            
            // Resumen de las actividades y sus calificaciones
            List<Map<String, Object>> activitySummaries = plan.getActivities().stream()
                .map(activity -> {
                    Map<String, Object> summary = new HashMap<>();
                    summary.put("name", activity.getName());
                    summary.put("percentage", activity.getPercentage());
                    summary.put("grade", activity.getGrade() != null ? activity.getGrade() : "Sin calificar");
                    return summary;
                })
                .collect(Collectors.toList());
            
            model.addAttribute("plan", plan);
            model.addAttribute("activitySummaries", activitySummaries);
            
            return "reports/planReport";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al generar el informe del plan: " + e.getMessage());
            return "redirect:/reports";
        }
    }

    // Informe general de notas y promedios
    @GetMapping("/general")
    public String generalReport(Model model) {
        try {
            String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<EvaluationPlan> userPlans = planService.getPlansByStudentId(studentEmail);
            
            // Calcular estadísticas generales
            Map<String, Object> stats = new HashMap<>();
            List<Double> allGrades = userPlans.stream()
                .flatMap(plan -> plan.getActivities().stream())
                .filter(activity -> activity.getGrade() != null && activity.getGrade() > 0)
                .map(Activities::getGrade)
                .collect(Collectors.toList());
            
            if (!allGrades.isEmpty()) {
                stats.put("averageGrade", allGrades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
                stats.put("maxGrade", allGrades.stream().mapToDouble(Double::doubleValue).max().orElse(0.0));
                stats.put("minGrade", allGrades.stream().mapToDouble(Double::doubleValue).min().orElse(0.0));
            } else {
                stats.put("averageGrade", 0.0);
                stats.put("maxGrade", 0.0);
                stats.put("minGrade", 0.0);
            }

            model.addAttribute("stats", stats);
            return "reports/generalReport";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al generar el informe general: " + e.getMessage());
            return "redirect:/reports";
        }
    }

    

    // Agregar estos 4 métodos al SimpleReportsController existente

// 1. Reporte de Actividades Sin Calificar
@GetMapping("/ungraded")
public String ungradedActivitiesReport(Model model) {
    try {
        String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<EvaluationPlan> userPlans = planService.getPlansByStudentId(studentEmail);
        
        List<Map<String, Object>> ungradedActivitiesByPlan = new ArrayList<>();
        
        for (EvaluationPlan plan : userPlans) {
            List<Activities> ungradedActivities = plan.getActivities().stream()
                .filter(activity -> activity.getGrade() == null || activity.getGrade() == 0)
                .collect(Collectors.toList());
            
            if (!ungradedActivities.isEmpty()) {
                Map<String, Object> planData = new HashMap<>();
                planData.put("planTitle", plan.getTitle());
                planData.put("planId", plan.getId());
                planData.put("ungradedActivities", ungradedActivities);
                planData.put("ungradedCount", ungradedActivities.size());
                ungradedActivitiesByPlan.add(planData);
            }
        }
        
        int totalUngradedActivities = ungradedActivitiesByPlan.stream()
            .mapToInt(plan -> (Integer) plan.get("ungradedCount"))
            .sum();
        
        model.addAttribute("ungradedActivitiesByPlan", ungradedActivitiesByPlan);
        model.addAttribute("totalUngradedActivities", totalUngradedActivities);
        
        return "reports/ungradedReport";
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error al generar el informe de actividades sin calificar: " + e.getMessage());
        return "redirect:/reports";
    }
}

// 2. Reporte de Rendimiento por Plan
@GetMapping("/performance")
public String performanceReport(Model model) {
    try {
        String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<EvaluationPlan> userPlans = planService.getPlansByStudentId(studentEmail);
        
        List<Map<String, Object>> planPerformances = userPlans.stream()
            .map(plan -> {
                Map<String, Object> performance = new HashMap<>();
                performance.put("planTitle", plan.getTitle());
                performance.put("planId", plan.getId());
                performance.put("groupId", plan.getGroupId());
                
                List<Activities> gradedActivities = plan.getActivities().stream()
                    .filter(activity -> activity.getGrade() != null && activity.getGrade() > 0)
                    .collect(Collectors.toList());
                
                double totalActivities = plan.getActivities().size();
                double gradedCount = gradedActivities.size();
                double completionPercentage = totalActivities > 0 ? (gradedCount / totalActivities) * 100 : 0;
                
                double weightedAverage = 0.0;
                if (!gradedActivities.isEmpty()) {
                    weightedAverage = gradedActivities.stream()
                        .mapToDouble(activity -> activity.getGrade() * (activity.getPercentage() / 100.0))
                        .sum();
                }
                
                performance.put("completionPercentage", Math.round(completionPercentage * 100.0) / 100.0);
                performance.put("weightedAverage", Math.round(weightedAverage * 100.0) / 100.0);
                performance.put("totalActivities", (int) totalActivities);
                performance.put("gradedActivities", (int) gradedCount);
                
                return performance;
            })
            .sorted((p1, p2) -> Double.compare((Double) p2.get("weightedAverage"), (Double) p1.get("weightedAverage")))
            .collect(Collectors.toList());
        
        model.addAttribute("planPerformances", planPerformances);
        
        return "reports/performanceReport";
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error al generar el informe de rendimiento: " + e.getMessage());
        return "redirect:/reports";
    }
}

// 3. Reporte de Actividades por Rango de Notas
@GetMapping("/grade-ranges")
public String gradeRangesReport(Model model) {
    try {
        String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<EvaluationPlan> userPlans = planService.getPlansByStudentId(studentEmail);
        
        // Definir rangos de notas
        Map<String, List<Map<String, Object>>> gradeRanges = new HashMap<>();
        gradeRanges.put("Excelente (4.5 - 5.0)", new ArrayList<>());
        gradeRanges.put("Bueno (4.0 - 4.4)", new ArrayList<>());
        gradeRanges.put("Aceptable (3.0 - 3.9)", new ArrayList<>());
        gradeRanges.put("Deficiente (0.0 - 2.9)", new ArrayList<>());
        
        for (EvaluationPlan plan : userPlans) {
            for (Activities activity : plan.getActivities()) {
                if (activity.getGrade() != null && activity.getGrade() > 0) {
                    Map<String, Object> activityInfo = new HashMap<>();
                    activityInfo.put("activityName", activity.getName());
                    activityInfo.put("planTitle", plan.getTitle());
                    activityInfo.put("grade", activity.getGrade());
                    activityInfo.put("percentage", activity.getPercentage());
                    
                    double grade = activity.getGrade();
                    if (grade >= 4.5) {
                        gradeRanges.get("Excelente (4.5 - 5.0)").add(activityInfo);
                    } else if (grade >= 4.0) {
                        gradeRanges.get("Bueno (4.0 - 4.4)").add(activityInfo);
                    } else if (grade >= 3.0) {
                        gradeRanges.get("Aceptable (3.0 - 3.9)").add(activityInfo);
                    } else {
                        gradeRanges.get("Deficiente (0.0 - 2.9)").add(activityInfo);
                    }
                }
            }
        }
        
        model.addAttribute("gradeRanges", gradeRanges);
        
        return "reports/gradeRangesReport";
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error al generar el informe por rangos de notas: " + e.getMessage());
        return "redirect:/reports";
    }
}

// 4. Reporte de Comentarios por Plan
@GetMapping("/comments-summary")
public String commentsSummaryReport(Model model) {
    try {
        String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<EvaluationPlan> userPlans = planService.getPlansByStudentId(studentEmail);
        
        List<Map<String, Object>> planCommentsSummary = userPlans.stream()
            .map(plan -> {
                Map<String, Object> summary = new HashMap<>();
                summary.put("planTitle", plan.getTitle());
                summary.put("planId", plan.getId());
                summary.put("groupId", plan.getGroupId());
                
                int commentCount = plan.getComments() != null ? plan.getComments().size() : 0;
                summary.put("commentCount", commentCount);
                
                // Obtener los comentarios más recientes (máximo 3)
                List<Comment> recentComments = new ArrayList<>();
                if (plan.getComments() != null && !plan.getComments().isEmpty()) {
                    recentComments = plan.getComments().stream()
                        .sorted((c1, c2) -> c2.getTimestamp().compareTo(c1.getTimestamp()))
                        .limit(3)
                        .collect(Collectors.toList());
                }
                summary.put("recentComments", recentComments);
                
                return summary;
            })
            .sorted((p1, p2) -> Integer.compare((Integer) p2.get("commentCount"), (Integer) p1.get("commentCount")))
            .collect(Collectors.toList());
        
        int totalComments = planCommentsSummary.stream()
            .mapToInt(plan -> (Integer) plan.get("commentCount"))
            .sum();
        
        model.addAttribute("planCommentsSummary", planCommentsSummary);
        model.addAttribute("totalComments", totalComments);
        
        return "reports/commentsSummaryReport";
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error al generar el informe de comentarios: " + e.getMessage());
        return "redirect:/reports";
    }
}
}
