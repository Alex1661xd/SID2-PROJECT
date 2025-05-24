package com.trackademic.controller.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trackademic.model.mongo.EvaluationPlan;
import com.trackademic.service.mongo.EvaluationPlanService;

@Controller
@RequestMapping("/evaluation-plans")
public class EvaluationPlanController {

    @Autowired
    private EvaluationPlanService planService;

    @GetMapping
    public String listPlans(Model model) {
        List<EvaluationPlan> plans = planService.getAllPlans();
        model.addAttribute("plans", plans);
        return "evaluationPlans/list";
    }


    @GetMapping
    public String showCreateForm(Model model) {
        model.addAttribute("plan", new EvaluationPlan());
        return "evaluationPlans/form";
    }

    @PostMapping
    public String savePlan(@ModelAttribute EvaluationPlan plan, Model model) {
        try {
            planService.savePlan(plan);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("plan", plan);
            return "evaluationPlans/form";
        }
        return "redirect:/evaluation-plans";
    }


    @GetMapping("/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        EvaluationPlan plan = planService.getPlanById(id).orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
        model.addAttribute("plan", plan);
        return "evaluationPlans/form";
    }

    @GetMapping("/{id}")
    public String deletePlan(@PathVariable String id) {
        planService.deletePlan(id);
        return "redirect:/evaluation-plans";
    }
}
