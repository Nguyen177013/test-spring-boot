package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc

public class StudentControllerTestContainer  {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("pgvector/pgvector:pg16")
            .withUsername("username")
            .withPassword("password")
            .withDatabaseName("sa");
    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StudentRepository repository;

    private StudentEntity student;
    @BeforeEach
    void setUp(){
        student = StudentEntity.builder()
                .firstName("Nguyen")
                .lastName("Phan")
                .email("nguyen@gmail.com")
                .address("eladon")
                .build();
    }
    @Test
    public void givenStudentObject_whenCreateStudent_thenReturnSavedStudent() throws Exception {
        // given - prevondition or setup
        // when - action or behaviour that we are going to test
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/student/create-student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));
        //  then - verify out put
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(student.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(student.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(student.getEmail())));
    }
    @Test
    public void given_whenGetAllStudent_thenReturnPaginationOfStudent() throws Exception {
        // given - prevondition or setup
        StudentEntity student2 = StudentEntity.builder()
                .id(2L)
                .firstName("Phuong")
                .lastName("Dang Xuan")
                .email("phuong.yoshino@gmail.com")
                .address("Go vap")
                .build();
//        repository.saveAll(List.of(student, student2));
        // when - action or behaviour that we are going to test
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/student/get-all-student")
                .param("page", "1")
                .param("size", "5"));
        //  then - verify out put
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", CoreMatchers.is(2)));
    }

    @Test
    public void givenStudentId_whenGetStudentById_thenReturnStudent() throws Exception {
        // given - prevondition or setup
        StudentEntity newStudent = repository.save(student);
        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/student/get-student/{id}", newStudent.getId()));
        //  then - verify out put
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(student.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(student.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(student.getEmail())));
    }

    @Test
    public void givenStudentId_whenGetStudentById_thenReturnNoFound() throws Exception {
        // given - prevondition or setup
        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/student/get-student/{id}", student.getId()));
        //  then - verify out put
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenStudentIdAndStudentObject_whenUpdateStudentById_thenUpdatedStudent() throws Exception {
        // given - prevondition or setup
        StudentEntity newStudent = repository.save(student);
        newStudent.setEmail("ahaha");
        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/student/update-student/{id}", newStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStudent)));
        //  then - verify out put
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(newStudent.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(newStudent.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(newStudent.getEmail())));
    }

    @Test
    public void givenStudentIdAndStudentObject_whenUpdateStudentById_thenReturnNotFound() throws Exception {
        // given - prevondition or setup
        student.setEmail("ahaha");
        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/student/update-student/{id}", student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));
        //  then - verify out put
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenStudentId_whenRemoveStudent_thenRemovedStudent() throws Exception {
        // given - prevondition or setup
        StudentEntity newStudent = repository.save(student);
        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/student/remove-student/{id}", newStudent.getId()));
        //  then - verify out put
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
