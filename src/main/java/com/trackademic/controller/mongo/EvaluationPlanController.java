package com.trackademic.controller.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.trackademic.service.postgres.GroupService;
import com.trackademic.model.mongo.EvaluationPlan;
import com.trackademic.service.mongo.EvaluationPlanService;

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
        model.addAttribute("plans", plans);
        return "evaluationPlans/list";
    }

    @GetMapping("/create")
    public String showCreateForm(int id, Model model) {
        model.addAttribute("plan", new EvaluationPlan());
        model.addAttribute("groupId", groupService.getIdentifierGroupById(id));
        return "evaluationPlans/form";
    }

     @GetMapping("/group/{groupId}")
    public String getPlansByGroup(@PathVariable String groupId, Model model) {
        List<EvaluationPlan> plans = planService.getPlansByGroupId(groupId);
        model.addAttribute("plans", plans);
        return "plans/list";  // página Thymeleaf o JSP que muestre la lista
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

    @DeleteMapping("/{id}")
    public String deletePlan(@PathVariable String id) {
        planService.deletePlan(id);
        return "redirect:/evaluation-plans";
    }
}
