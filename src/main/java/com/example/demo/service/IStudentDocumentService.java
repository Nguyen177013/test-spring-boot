package com.example.demo.service;

import com.example.demo.StudentDto.StudentDocumentDto;
import com.example.demo.entity.StudentDocumentEntity;
import reactor.core.publisher.Mono;

public interface IStudentDocumentService {
    Mono<StudentDocumentDto> saveStudent(StudentDocumentEntity student);
}
