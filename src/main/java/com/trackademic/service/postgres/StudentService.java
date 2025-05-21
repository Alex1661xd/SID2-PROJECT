package com.trackademic.service.postgres;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trackademic.model.postgres.Student;
import com.trackademic.repository.postgres.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService implements UserDetailsService{
   private final StudentRepository studentRepository;
   private final PasswordEncoder passwordEncoder;
   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Student student = studentRepository.findByEmail(email)
            .orElseThrow(() -> {
                return new UsernameNotFoundException("Student not found: " + email);
            });
        return student;
   }

   @Transactional
   public void registerStudent(Student student) {
    student.setPassword(passwordEncoder.encode(student.getPassword()));
    studentRepository.save(student);
   }
    
}
