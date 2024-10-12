package com.example.demo.repository;

import com.example.demo.StudentDto.StudentDto;
import com.example.demo.entity.StudentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTests {
    @Autowired
    private StudentRepository repository;

    private StudentEntity student;

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
    public void givenStudentObject_whenSave_thenSavedStudent(){
        // given - prevondition or setup
        // when - action or behaviour that we are going to test
        StudentEntity result = repository.save(student);
        //  then - verify out put
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    public void givenStudentId_whenFindById_thenReturnStudentById(){
        // given - prevondition or setup
        StudentEntity studentEntity= repository.save(student);
        // when - action or behaviour that we are going to test
        StudentDto studentDto = modelMapper.map(repository.findById(studentEntity.getId()).get(), StudentDto.class);
        //  then - verify out put
        assertThat(studentDto).isNotNull();
        assertThat(studentDto.getFirstName()).isEqualTo("Nguyen");
    }
    @Test
    public void givenStudentList_whenGetAll_thenReturnListStudent(){
        // given - prevondition or setup
        StudentEntity student2 = StudentEntity.builder()
                .id(2L)
                .firstName("Phuong")
                .lastName("Dang Xuan")
                .email("phuong.yoshino@gmail.com")
                .address("Go vap")
                .build();
        repository.save(student);
        repository.save(student2);
        // when - action or behaviour that we are going to test
        List<StudentEntity> listStudent = repository.findAll();
        //  then - verify out put
        assertThat(listStudent).isNotNull();
        assertThat(listStudent.size()).isEqualTo(2);
    }
    @Test
    public void givenStudentUpdate_whenUpdateStudent_thenUpdatedStudent(){
        // given - prevondition or setup
        repository.save(student);
        student.setEmail("doraemon@gmail.com");
        student.setAddress("ahaha");
        // when - action or behaviour that we are going to test
        StudentEntity result = repository.save(student);
        //  then - verify out put
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isNotEqualTo("nguyen@gmail.com");
        assertThat(result.getEmail()).isEqualTo("doraemon@gmail.com");
    }
    @Test
    public void givenStudentEmail_whenFindByEmail_thenReturnStudent(){
        // given - prevondition or setup
        repository.save(student);
        // when - action or behaviour that we are going to test
        StudentEntity student = repository.findStudentByEmail(this.student.getEmail()).get();
        //  then - verify out put
        assertThat(student).isNotNull();
    }
    @Test
    public void givenStudentId_whenRemoveStudentById_thenRemoveStudent(){
        // given - prevondition or setup

        // when - action or behaviour that we are going to test
        repository.deleteById(student.getId());
        List<StudentEntity> studentEntityList = repository.findAll();
        //  then - verify out put
        assertThat(studentEntityList).isEmpty();
    }
}
