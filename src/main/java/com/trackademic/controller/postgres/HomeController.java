package com.trackademic.controller.postgres;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trackademic.model.postgres.Student;
import com.trackademic.service.postgres.GroupService;
import com.trackademic.service.postgres.StudentService;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final GroupService groupService;

    private final StudentService studentService;

    public HomeController(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService=studentService;
    }

    @GetMapping
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String studentEmail = userDetails.getUsername();
        Student student=studentService.getStudentbyid(studentEmail);
        model.addAttribute("name", student.getFirstName());
        model.addAttribute("lastname", student.getLastName());
        return "StudentHome/home";
    }

    @GetMapping("/joingroup")
    public String showGroups(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String studentEmail = userDetails.getUsername();
        model.addAttribute("groups", groupService.findGroupsNotJoinedByStudent(studentEmail));
        return "StudentHome/joinGroup"; // Vista para mostrar solo grupos disponibles para unirse
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
