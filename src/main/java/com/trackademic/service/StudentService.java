package com.trackademic.service;

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
       return studentRepository.findByEmail(email)
               .orElseThrow(() -> new UsernameNotFoundException("Student not found with email: " + email));
   }

   @Transactional
   public void registerStudent(Student student) {
       // Encode the password before saving
       student.setPassword(passwordEncoder.encode(student.getPassword()));
       studentRepository.save(student);
   }
    
}
