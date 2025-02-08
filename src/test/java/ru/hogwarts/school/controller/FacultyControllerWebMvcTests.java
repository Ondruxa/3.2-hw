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
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacultyService facultyService;

    @MockitoBean
    private AvatarService avatarService;

    @Test
    void shouldGetCreateFaculty() throws Exception {
        Long facultyId = 2L;
        Faculty faculty = new Faculty("Griffindor", "red");
        Faculty savedFaculty = new Faculty("Griffindor", "red");
        savedFaculty.setId(facultyId);
        when(facultyService.createFaculty(faculty)).thenReturn(savedFaculty);

        ResultActions perform = mockMvc.perform(
                post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(jsonPath("$.id").value(savedFaculty.getId()))
                .andExpect(jsonPath("$.name").value(savedFaculty.getName()))
                .andExpect(jsonPath("$.color").value(savedFaculty.getColor()))
                .andDo(print());

    }

    @Test
    void shouldGetFacultyInfo() throws Exception {
        Long facultyId = 2L;
        Faculty faculty = new Faculty("Griffindor", "red");

        when(facultyService.findFaculty(facultyId)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(get("/faculty/{id}", facultyId));

        perform
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    void shouldGetStudentsOfFaculty() throws Exception {
        Long facultyId = 1L;
        Long studentId1 = 1L;
        Long studentId2 = 2L;
        Faculty faculty = new Faculty(facultyId, "testColor", "testName");
        Student student1 = new Student(studentId1, "Ivan", 13);
        Student student2 = new Student(studentId2, "Roman", 11);
        student1.setFaculty(faculty);
        student2.setFaculty(faculty);
        List<Student> studentsOfFaculty = new ArrayList<>();
        studentsOfFaculty.add(student1);
        studentsOfFaculty.add(student2);
        faculty.setStudents(studentsOfFaculty);

        when(facultyService.findFaculty(facultyId)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(get("/faculty/{id}/student", facultyId));

        perform
                .andExpect(jsonPath("$[0].id").value(faculty.getStudents().get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(faculty.getStudents().get(0).getName()))
                .andExpect(jsonPath("$[0].age").value(faculty.getStudents().get(0).getAge()))
                .andExpect(jsonPath("$[1].id").value(faculty.getStudents().get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(faculty.getStudents().get(1).getName()))
                .andExpect(jsonPath("$[1].age").value(faculty.getStudents().get(1).getAge()))
                .andDo(print());
    }

    @Test
    void shouldFindFaculties() throws Exception {
        Long facultyId1 = 2L;
        Long facultyId2 = 2L;
        Faculty faculty1 = new Faculty(facultyId1, "Griffindor", "red");
        Faculty faculty2 = new Faculty(facultyId2, "tdg", "red");
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty1);
        faculties.add(faculty2);

        when(facultyService.findByNameOrColor(faculty1.getName(), faculty1.getColor())).thenReturn(faculties);

        ResultActions perform = mockMvc.perform(get("/faculty?name=Griffindor&color=red"));

        perform
                .andExpect(jsonPath("$[0].name").value(faculties.get(0).getName()))
                .andExpect(jsonPath("$[0].id").value(faculties.get(0).getId()))
                .andExpect(jsonPath("$[0].color").value(faculties.get(0).getColor()))
                .andExpect(jsonPath("$[1].name").value(faculties.get(1).getName()))
                .andExpect(jsonPath("$[1].id").value(faculties.get(1).getId()))
                .andExpect(jsonPath("$[1].color").value(faculties.get(1).getColor()))
                .andDo(print());
    }

    @Test
    void shouldEditFaculty() throws Exception {
        Long facultyId = 2L;
        Faculty faculty = new Faculty("Griffindor", "red");

        when(facultyService.editFaculty(faculty)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(
                put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    void shouldDeleteFaculty() throws Exception {
        Long facultyId = 1L;

        ResultActions perform = mockMvc.perform(
                delete("/faculty/" + facultyId));

        perform
                .andExpect(status().isOk());
        verify(facultyService).deleteFaculty(facultyId);
    }
}
