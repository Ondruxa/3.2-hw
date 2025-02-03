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
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


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
}
