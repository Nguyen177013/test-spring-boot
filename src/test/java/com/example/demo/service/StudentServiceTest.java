package com.example.demo.service;

import com.example.demo.StudentDto.StudentDto;
import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.imps.StudentServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentServiceImp service;

    private StudentEntity student;

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
    public void givenStudentData_whenSaveStudent_thenReturnStudentObject(){
        // given - prevondition or setup
        given(repository.save(student)).willReturn(student);
        // when - action or behaviour that we are going to test
        StudentDto studentDto = service.createStudent(student);
        //  then - verify out put
        assertThat(studentDto).isNotNull();
    }
    @Test
    public void givenStudentId_whenFindStudentById_thenReturnStudent(){
        // given - prevondition or setup
        given(repository.findById(student.getId())).willReturn(Optional.of(student));
        // when - action or behaviour that we are going to test
        StudentDto student = service.getStudentById(this.student.getId());
        //  then - verify out put
        assertThat(student).isNotNull();
    }

    @Test
    public void givenStudentId_whenFindStudentById_thenReturnNull(){
        // given - prevondition or setup
        given(repository.findById(student.getId())).willReturn(Optional.empty());
        // when - action or behaviour that we are going to test
        StudentDto student = service.getStudentById(this.student.getId());
        //  then - verify out put
        assertThat(student).isNull();
    }
    @Test
    public void given_whenGetAllStudent_thenReturnListStudent(){
        // given - prevondition or setup
        StudentEntity student2 = StudentEntity.builder()
                .id(2L)
                .firstName("Phuong")
                .lastName("Dang Xuan")
                .email("phuong.yoshino@gmail.com")
                .address("Go vap")
                .build();
        given(repository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(List.of(student, student2)));
        // when - action or behaviour that we are going to test
        Page<StudentEntity> listStudent = service.getAllStudent(PageRequest.of(1, 5));
        //  then - verify out put
        assertThat(listStudent).isNotNull();
        assertThat(listStudent.getTotalElements()).isEqualTo(2);
    }
    @Test
    public void given_whenGetAllStudent_thenReturnEmptyListStudent(){
        // given - prevondition or setup
        given(repository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(List.of()));
        // when - action or behaviour that we are going to test
        Page<StudentEntity> listStudent = service.getAllStudent(PageRequest.of(1, 5));
        //  then - verify out put
        assertThat(listStudent).isNotNull();
        assertThat(listStudent.getTotalElements()).isEqualTo(0);
    }
    @Test
    public void givenUpdateStudent_whenUpdateStudent_thenReturnUpdatedStudent(){
        // given - prevondition or setup
        given(repository.findById(student.getId())).willReturn(Optional.of(student));
        student.setEmail("updated");
        given(repository.save(student)).willReturn(student);
        // when - action or behaviour that we are going to test
        StudentDto studentDto = service.updateStudent(student.getId(),student);
        //  then - verify out put
        assertThat(studentDto).isNotNull();
        assertThat(studentDto.getEmail()).isEqualTo("updated");
    }
    @Test
    public void givenStudentId_wheDeleteStudent_thenRemoveStudent(){
        // given - prevondition or setup
        willDoNothing().given(repository).deleteById(student.getId());
        // when - action or behaviour that we are going to test
        service.deleteStudent(student.getId());
        //  then - verify out put
        verify(repository, times(1)).deleteById(student.getId());
    }
}
