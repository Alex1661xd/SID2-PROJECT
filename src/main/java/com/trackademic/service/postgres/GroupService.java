package com.trackademic.service.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.trackademic.model.postgres.Group;
import com.trackademic.model.postgres.GroupEnrollment;
import com.trackademic.model.postgres.GroupId;
import com.trackademic.model.postgres.Student;
import com.trackademic.repository.postgres.GroupEnrollmentRepository;
import com.trackademic.repository.postgres.GroupRepository;
import com.trackademic.repository.postgres.StudentRepository;



@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GroupEnrollmentRepository groupEnrollmentRepository; // debe existir

    public GroupService(GroupRepository groupRepository,StudentRepository studentRepository, GroupEnrollmentRepository groupEnrollmentRepository) {
        this.groupRepository = groupRepository;
        this.groupEnrollmentRepository = groupEnrollmentRepository;
        this.studentRepository=studentRepository;
    }

    public List<Group> showAllGroups() {
        return groupRepository.findAll();
    }

   public boolean joinGroup(Integer number, String subjectCode, String semester, String studentEmail) {
    Optional<Group> groupOpt = groupRepository.findById(new GroupId(number, subjectCode, semester));
    Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);

    System.out.println("Buscando grupo: " + number + " " + subjectCode + " " + semester + " => " + groupOpt.isPresent());
    System.out.println("Buscando estudiante con email: " + studentEmail + " => " + studentOpt.isPresent());

    if (groupOpt.isPresent() && studentOpt.isPresent()) {
        GroupEnrollment enrollment = new GroupEnrollment();
        enrollment.setGroupNumber(number);
        enrollment.setGroupSubjectCode(subjectCode);
        enrollment.setGroupSemester(semester);
        enrollment.setStudent(studentOpt.get().getId());
        enrollment.setGroup(groupOpt.get());
        enrollment.setStudentEntity(studentOpt.get());

        groupEnrollmentRepository.save(enrollment);
        return true;
    }
    return false;
}


public List<GroupEnrollment> getEnrollmentsByStudentEmail(String email) {
    Optional<Student> studentOpt = studentRepository.findByEmail(email);
    if (studentOpt.isEmpty()) {
        return List.of();
    }
    Student student = studentOpt.get();
    // Aquí devolvemos la lista de enrollments directamente
    return groupEnrollmentRepository.findByStudent(student.getId());
}

}
