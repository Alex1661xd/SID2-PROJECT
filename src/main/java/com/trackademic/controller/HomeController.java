package com.trackademic.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trackademic.service.postgres.GroupService;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final GroupService groupService;

    public HomeController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public String home() {
        return "StudentHome/home";
    }

    @GetMapping("/joingroup")
    public String showGroups(Model model) {
        model.addAttribute("groups", groupService.showAllGroups());
        return "StudentHome/joinGroup"; // Nueva vista para mostrar grupos
    }

    @PostMapping("/joingroup")
    public String joinGroup(
        @RequestParam String groupId,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        String[] parts = groupId.split("\\|");
        Integer number = Integer.valueOf(parts[0]);
        String subjectCode = parts[1];
        String semester = parts[2];

        String studentEmail = userDetails.getUsername();
        boolean joined = groupService.joinGroup(number, subjectCode, semester, studentEmail);

        if (joined) {
            model.addAttribute("message", "Te has unido al grupo exitosamente.");
        } else {
            model.addAttribute("error", "No fue posible unirte al grupo.");
        }

        return "StudentHome/joinGroupResult";
    }

    @GetMapping("/mygroups")
    public String myGroups(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String studentEmail = userDetails.getUsername();
        var enrollments = groupService.getEnrollmentsByStudentEmail(studentEmail);
        model.addAttribute("enrollments", enrollments);
        return "StudentHome/myGroups";
    }

}
