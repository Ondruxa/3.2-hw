package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FacultyControllerTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void clearDatabase() {
        facultyRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    void shouldCreateFaculty() {
        Faculty faculty = new Faculty("Griffinfor", "red");

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class
        );

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertNotNull(actualFaculty.getId());
        assertEquals(actualFaculty.getName(), faculty.getName());
        assertThat(actualFaculty.getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    void shouldGetFaculty() {
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
                Faculty.class
        );

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.getId(), faculty.getId());
        assertEquals(actualFaculty.getName(), faculty.getName());
        assertEquals(actualFaculty.getColor(), faculty.getColor());
    }

    @Test
    void shouldGetStudentsOfFaculty() {
        Faculty faculty = new Faculty("testName", "testColor");
        faculty = facultyRepository.save(faculty);
        Student student1 = new Student("name1", 15);
        Student student2 = new Student("name2", 17);
        student1.setFaculty(faculty);
        student2.setFaculty(faculty);
        student1 = studentRepository.save(student1);
        student2 = studentRepository.save(student2);


        ResponseEntity<List<Student>> response = restTemplate.exchange("/faculty/" + faculty.getId() + "/student", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });

        List<Student> students = response.getBody();
        assertThat(students).hasSize(2);
        assertEquals(students.get(1).getName(), student2.getName());
        assertEquals(students.get(1).getAge(), student2.getAge());
        assertEquals(students.get(1).getId(), student2.getId());
        studentRepository.deleteAll();
    }

    @Test
    void shouldFindFaculties() {
        Faculty faculty1 = new Faculty("name1", "color1");
        Faculty faculty2 = new Faculty("name1", "color1");
        faculty1 = facultyRepository.save(faculty1);
        faculty2 = facultyRepository.save(faculty2);


        ResponseEntity<List<Faculty>> response = restTemplate.exchange("/faculty?name=name1&color=color1", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Faculty>>() {
                });


        List<Faculty> faculties = response.getBody();
        assertThat(faculties).hasSize(2);
        assertEquals(faculties.get(1).getName(), faculty2.getName());
        assertEquals(faculties.get(1).getColor(), faculty2.getColor());
        assertEquals(faculties.get(1).getId(), faculty2.getId());

    }

    @Test
    public void testEditFaculty() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setName("test1");
        faculty1.setColor("pink");
        facultyRepository.save(faculty1);
        Faculty faculty2 = new Faculty();
        faculty2.setId(faculty1.getId());
        faculty2.setName("TEST2");
        faculty2.setColor("brown");


        ResponseEntity<Faculty> response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                new HttpEntity<>(faculty2),
                Faculty.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertThat(response.getBody().getColor())
                .isEqualTo("brown");
        assertThat(response.getBody().getName())
                .isEqualTo("TEST2");

        facultyRepository.deleteById(faculty2.getId());
    }

    @Test
    void shouldDeleteFaculty() {
        // given
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        // when
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        // then
        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        assertThat(facultyRepository.findById(faculty.getId())).isNotPresent();
    }
}
