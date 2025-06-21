package com.microservice.course.services;

import com.microservice.course.client.IStudentClient;
import com.microservice.course.dto.StudentDTO;
import com.microservice.course.entities.Course;
import com.microservice.course.http.response.StudentByCourseResponse;
import com.microservice.course.presistence.ICourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CourseServiceImpl implements  ICourseService{

    @Autowired
    private ICourseRepository courseRepository;
    @Autowired
    private IStudentClient studentClient;
    @Override
    public List<Course> findAll() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    @Override
    public void save(Course c) {
        courseRepository.save(c);
    }

    @Override
    public StudentByCourseResponse findStudentsByIdCourse(Long idCourse) {
        Course course = courseRepository.findById(idCourse).orElse(new Course());
        List<StudentDTO> studentDTOLsit=studentClient.findAllStudentsByCourse(idCourse);
        return StudentByCourseResponse.builder()
                .courseName(course.getName())
                .teacher(course.getTeacher())
                .studentDTOList(studentDTOLsit)
                .build();
    }
}
