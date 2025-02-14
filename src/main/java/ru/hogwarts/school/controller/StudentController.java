package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("studentAge")
    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) Integer age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("studentAgeBetween")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam(required = false) int min,
                                                                @RequestParam(required = false) int max) {

        return ResponseEntity.ok(studentService.findByAgeBetween(min, max));

    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> getFacultyOfStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.getFaculty());
    }

    @GetMapping("studentNumber")
    public Integer getNumberOfStudents() {
        Integer numberOfStudents = studentService.getNumberOfStudents();
        return numberOfStudents;
    }

    @GetMapping("AvgAgeOfStudents")
    public Integer getAvgAgeOfStudents() {
        Integer avgAgeOfStudents = studentService.getAvgAgeOfStudents();
        return avgAgeOfStudents;
    }

    @GetMapping("LastFiveStudents")
    public List<Student> getLastFiveStudents() {
        List<Student> lastStudents = studentService.getLastStudents();
        return lastStudents;
    }


    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}