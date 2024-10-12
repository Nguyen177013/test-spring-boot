package com.example.demo.service;

import com.example.demo.StudentDto.StudentDto;
import com.example.demo.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IStudentService {
    StudentDto createStudent(StudentEntity student);
    Page<StudentEntity> getAllStudent(Pageable pageable);
    StudentDto getStudentById(long studentId);
    StudentDto updateStudent(long studentId, StudentEntity studentEntity);
    boolean deleteStudent(long studentId);
}
