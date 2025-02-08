package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private AvatarService avatarService;

    @Test
    void shouldGetCreateStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student("Ivan", 20);
        Student savedStudent = new Student("Ivan", 20);
        savedStudent.setId(studentId);

        when(studentService.createStudent(student)).thenReturn(savedStudent);

        ResultActions perform = mockMvc.perform(
                post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)));

        perform
                .andExpect(jsonPath("$.id").value(savedStudent.getId()))
                .andExpect(jsonPath("$.name").value(savedStudent.getName()))
                .andExpect(jsonPath("$.age").value(savedStudent.getAge()))
                .andDo(print());

    }

    @Test
    void shouldGetStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student("Ivan", 20);

        when(studentService.findStudent(studentId)).thenReturn(student);

        ResultActions perform = mockMvc.perform(get("/student/{id}", studentId));

        perform
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    void shouldFindStudents() throws Exception {
        Long studentId1 = 1L;
        Long studentId2 = 2L;
        Student student1 = new Student("Ivan", 10);
        Student student2 = new Student("Roman", 10);
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        when(studentService.findByAge(10)).thenReturn(students);

        ResultActions perform = mockMvc.perform(get("/student/studentAge?age=10"));

        perform
                .andExpect(jsonPath("$[0].name").value(students.get(0).getName()))
                .andExpect(jsonPath("$[0].age").value(students.get(0).getAge()))
                .andExpect(jsonPath("$[1].name").value(students.get(1).getName()))
                .andExpect(jsonPath("$[1].age").value(students.get(1).getAge()));
    }

    @Test
    void shouldFindByAgeBetween() throws Exception {
        Long studentId1 = 1L;
        Long studentId2 = 2L;
        Student student1 = new Student("Ivan", 10);
        Student student2 = new Student("Roman", 15);
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        when(studentService.findByAgeBetween(9, 16)).thenReturn(students);

        ResultActions perform = mockMvc.perform(get("/student/studentAgeBetween?min=9&max=16"));

        perform
                .andExpect(jsonPath("$[0].name").value(students.get(0).getName()))
                .andExpect(jsonPath("$[0].age").value(students.get(0).getAge()))
                .andExpect(jsonPath("$[1].name").value(students.get(1).getName()))
                .andExpect(jsonPath("$[1].age").value(students.get(1).getAge()))
                .andDo(print());
    }

    @Test
    void shouldGetFacultyOfStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student("Ivan", 20);
        Faculty faculty = new Faculty(1L, "testColor", "testName");
        student.setFaculty(faculty);

        when(studentService.findStudent(studentId)).thenReturn(student);

        ResultActions perform = mockMvc.perform(get("/student/{id}/faculty", studentId));

        perform
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student("Ivan", 20);

        when(studentService.editStudent(student)).thenReturn(student);

        ResultActions perform = mockMvc.perform(
                put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)));

        perform
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    void shouldDeleteStudent() throws Exception {
        Long studentId = 1L;

        ResultActions perform = mockMvc.perform(
                delete("/student/" + studentId));

        perform
                .andExpect(status().isOk());
        verify(studentService).deleteStudent(studentId);
    }


}
