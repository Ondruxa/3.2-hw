package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class StudentContollerTestRestTemplate {


        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private StudentController studentController;

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private FacultyRepository facultyRepository;

        @LocalServerPort
        private int port;

        @BeforeEach
        public void clearDatabase() {
            studentRepository.deleteAll();
        }


        @Test
        void shouldCreateStudent() {
            Student student = new Student("name", 15);

            ResponseEntity<Student> studentResponseEntity = restTemplate.postForEntity(
                    "http://localhost:" + port + "/student",
                    student,
                    Student.class
            );

            assertNotNull(studentResponseEntity);
            assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
            Student actualStudent = studentResponseEntity.getBody();
            assertNotNull(actualStudent.getId());
            assertEquals(actualStudent.getName(), student.getName());
            org.assertj.core.api.Assertions.assertThat(actualStudent.getAge()).isEqualTo(student.getAge());
        }

        @Test
        void shouldGetStudent() {
            Student student = new Student("name", 15);
            student = studentRepository.save(student);
            
            ResponseEntity<Student> studentResponseEntity = restTemplate.getForEntity(
                    "http://localhost:" + port + "/student/" + student.getId(),
                    Student.class
            );

            assertNotNull(studentResponseEntity);
            assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

            Student actualStudent = studentResponseEntity.getBody();
            assertEquals(actualStudent.getId(), student.getId());
            assertEquals(actualStudent.getName(), student.getName());
            assertEquals(actualStudent.getAge(), student.getAge());
        }

    @Test
    void shouldFindStudents() {
        Student student1 = new Student("name1", 14);
        Student student2 = new Student("name2", 14);
        student1 = studentRepository.save(student1);
        student2 = studentRepository.save(student2);


        ResponseEntity<List<Student>> response = restTemplate.exchange("/student/studentAge?age=14", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });

        List<Student> students = response.getBody();
        assertThat(students).hasSize(2);
        assertEquals(students.get(1).getName(), student2.getName());
        assertEquals(students.get(0).getName(), student1.getName());
        assertEquals(students.get(1).getAge(), student2.getAge());
        assertEquals(students.get(0).getAge(), student1.getAge());
        assertEquals(students.get(1).getId(), student2.getId());
        assertEquals(students.get(0).getId(), student1.getId());

    }

    @Test
    void shouldFindByAgeBetween() {
        Student student1 = new Student("name1", 17);
        Student student2 = new Student("name2", 13);
        Student student3 = new Student("name3", 9);
        student1 = studentRepository.save(student1);
        student2 = studentRepository.save(student2);
        student3 = studentRepository.save(student3);


        ResponseEntity<List<Student>> response = restTemplate.exchange("/student/studentAgeBetween?min=10&max=17", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });

        List<Student> students = response.getBody();
        assertThat(students).hasSize(2);
        assertEquals(students.get(0).getName(), student1.getName());
        assertEquals(students.get(1).getName(), student2.getName());
        assertEquals(students.get(0).getAge(), student1.getAge());
        assertEquals(students.get(1).getAge(), student2.getAge());
        assertEquals(students.get(0).getId(), student1.getId());
        assertEquals(students.get(1).getId(), student2.getId());

    }

    @Test
    void shouldGetFacultyOfStudent() {
        Faculty faculty = new Faculty("testName", "testColor");
        faculty = facultyRepository.save(faculty);
        Student student1 = new Student("name1", 15);
        student1.setFaculty(faculty);
        student1 = studentRepository.save(student1);


        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + student1.getId() + "/faculty",
                Faculty.class
        );

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        Assertions
                .assertThat(facultyResponseEntity.getStatusCode().is2xxSuccessful());


        Faculty actual = facultyResponseEntity.getBody();
        assertEquals(actual.getId(), faculty.getId());
        assertEquals(actual.getName(), faculty.getName());
        assertEquals(actual.getColor(), faculty.getColor());


    }


        @Test
        public void testEditStudent() throws Exception {
            Student student1 = new Student();
            student1.setName("test1");
            student1.setAge(16);
            studentRepository.save(student1);
            Student student2 = new Student();
            student2.setId(student1.getId());
            student2.setName("TEST2");
            student2.setAge(17);


            ResponseEntity<Student> response = restTemplate.exchange("http://localhost:" + port + "/student",
                    HttpMethod.PUT,
                    new HttpEntity<>(student2),
                    Student.class);


            Assertions
                    .assertThat(response.getStatusCode().is2xxSuccessful());
            Assertions
                    .assertThat(response.getBody().getAge())
                    .isEqualTo(17);

            studentRepository.deleteById(student2.getId());
        }

        @Test
        void shouldDeleteStudent() {
            // given
            Student student = new Student("name", 15);
            student = studentRepository.save(student);

            // when
            ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                    "http://localhost:" + port + "/student/" + student.getId(),
                    HttpMethod.DELETE,
                    null,
                    Student.class
            );

            // then
            assertNotNull(studentResponseEntity);
            assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

            org.assertj.core.api.Assertions.assertThat(studentRepository.findById(student.getId())).isNotPresent();
        }
    }
