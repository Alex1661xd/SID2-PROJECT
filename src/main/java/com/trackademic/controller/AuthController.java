package com.trackademic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.trackademic.model.postgres.Student;
import com.trackademic.service.StudentService;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class AuthController {

    private final StudentService studentService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("student", new Student());
        // Add lists of campuses and cities if needed for dropdowns
        // model.addAttribute("campuses", campusRepository.findAll());
        // model.addAttribute("cities", cityRepository.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(@ModelAttribute Student student) {
        studentService.registerStudent(student);
        return "redirect:/login?success";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
    
    
}
