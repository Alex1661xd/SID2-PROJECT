package com.trackademic.service;

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

   private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    logger.info("Attempting to load user by email: {}", email);
    Student student = studentRepository.findByEmail(email)
            .orElseThrow(() -> {
                logger.error("Student not found: {}", email);
                return new UsernameNotFoundException("Student not found: " + email);
            });
    logger.info("Successfully loaded user: {}", email);
    return student;
   }

   @Transactional
   public void registerStudent(Student student) {
    logger.info("Registering student with email: {}", student.getEmail());
    student.setPassword(passwordEncoder.encode(student.getPassword()));
    studentRepository.save(student);
    logger.info("Student registered successfully: {}", student.getEmail());
   }
    
}
