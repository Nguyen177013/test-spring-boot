package com.example.demo.controller;

import com.example.demo.StudentDto.StudentDocumentDto;
import com.example.demo.entity.StudentDocumentEntity;
import com.example.demo.service.IStudentDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/student-document")
public class StudentDocumentController {
    @Autowired
    private IStudentDocumentService service;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<StudentDocumentDto> saveStudent(@RequestBody StudentDocumentEntity student){
        return service.saveStudent(student)
                .onErrorResume(e -> {
                    e.printStackTrace(); // For debugging, but better use a logger
                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving student", e));
                });
    }
}
