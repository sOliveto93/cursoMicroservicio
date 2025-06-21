package com.microservice.student.service;

import com.microservice.student.entities.Student;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IStudentService {
    List<Student> findAll();
    Student findById(Long id);
    void save(Student s);
    List<Student> findByIdCourse(Long idCourse);
}
