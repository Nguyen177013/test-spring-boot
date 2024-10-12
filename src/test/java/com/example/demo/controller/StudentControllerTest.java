package com.example.demo.controller;

import com.example.demo.StudentDto.StudentDto;
import com.example.demo.entity.StudentEntity;
import com.example.demo.service.imps.StudentServiceImp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
@WebMvcTest
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentServiceImp service;

    @Autowired
    private ObjectMapper objectMapper;

    StudentEntity student;
    ModelMapper modelMapper = new ModelMapper();
    @BeforeEach
    public void setUp(){
        student = StudentEntity.builder()
                .id(1L)
                .firstName("Nguyen")
                .lastName("Phan")
                .email("nguyen@gmail.com")
                .address("eladon")
                .build();
    }

    @Test
    public void givenStudentObject_whenCreateStudent_thenReturnSavedStudent() throws Exception {
        // given - prevondition or setup
        given(service.createStudent(ArgumentMatchers.any(StudentEntity.class)))
                .willAnswer((invocation -> {
                    StudentEntity entity = invocation.getArgument(0);
                    return modelMapper.map(entity, StudentDto.class);
                }));
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
        PageImpl page = new PageImpl<>(List.of(student, student2));
        given(service.getAllStudent(PageRequest.of(1, 5))).willReturn(page);
        // when - action or behaviour that we are going to test
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/student/get-all-student")
                .param("page", "1")
                .param("size", "5"));
        Object test = response.andReturn();
        //  then - verify out put
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", CoreMatchers.is((int)page.getTotalElements())));
    }
}
