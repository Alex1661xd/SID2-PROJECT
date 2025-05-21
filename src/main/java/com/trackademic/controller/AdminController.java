package com.trackademic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.trackademic.repository.postgres.*;
import com.trackademic.model.postgres.Employee;
import com.trackademic.model.postgres.Faculty;
import com.trackademic.model.postgres.Group;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmployeeRepository employeeRepository;

    private final SubjectRepository subjectRepository;

    private final GroupRepository groupRepository;

    private final FacultyRepository facultyRepository;

    private final CityRepository cityRepository;

    public AdminController(FacultyRepository facultyRepository,CityRepository cityRepository,EmployeeRepository employeeRepository,GroupRepository groupRepository, SubjectRepository subjectRepository) {
        this.employeeRepository = employeeRepository;
        this.groupRepository=groupRepository;
        this.subjectRepository=subjectRepository;
        this.facultyRepository=facultyRepository;
        this.cityRepository=cityRepository;
    }
    @GetMapping
    public String home() {
        return "AdminHome/adminHome";
    }


    @GetMapping("/create-group")
    public String showCreateGroupForm(Model model) {
        model.addAttribute("group", new Group());
        model.addAttribute("professors", employeeRepository.findAll());
        model.addAttribute("subjects", subjectRepository.findAll());
        return "AdminHome/createGroup";
    }

   @PostMapping("/create-group")
    public String createGroup(@ModelAttribute Group group) {
        Employee prof = employeeRepository.findById(group.getProfessorId())
                                        .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
        group.setProfessor(prof);
        groupRepository.save(group);
        return "redirect:/admin?groupCreated";
    }


    @GetMapping("/create-faculty")
    public String showCreateFacultyForm(Model model) {
        model.addAttribute("faculty", new Faculty());
        model.addAttribute("employee", employeeRepository.findAll());
        model.addAttribute("city", cityRepository.findAll());
        return "AdminHome/createFaculty";
    }

   @PostMapping("/create-faculty")
    public String createFaculty(@ModelAttribute Faculty faculty) {
        Employee prof = employeeRepository.findById(faculty.getEmployeeId())
                                        .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
        faculty.setDean(prof);
        facultyRepository.save(faculty);
        return "redirect:/admin?facultyCreated";
    }


}
