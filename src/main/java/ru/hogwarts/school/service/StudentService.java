package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;
    Logger logger = LoggerFactory.getLogger(StudentRepository.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("debug of create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student");
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        logger.debug("debug of delete student");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        logger.info("Was invoked method for find by age students");
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find by age between students");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Integer getNumberOfStudents() {
        logger.info("Was invoked method for get number of students");
        return studentRepository.getNumberOfStudents();
    }

    public Integer getAvgAgeOfStudents() {
        logger.info("Was invoked method for get avg age of students");
        return studentRepository.getAvgAgeOfStudents();
    }

    public List<Student> getLastStudents() {
        logger.info("Was invoked method for get last student");
        return studentRepository.getLastFiveStudents();
    }

    public List<Student> getAllStudentsWithNameStartsWithA() {
        return studentRepository.findAll().stream().filter(s -> s.getName().toUpperCase().
                startsWith("A")).sorted().toList();
    }

    public ResponseEntity<Double> getAverageAgeStudents() {
        double listStudents = studentRepository.findAll().
                stream().mapToDouble(Student::getAge).average().orElseThrow();
        return ResponseEntity.ok(listStudents);
    }
}
