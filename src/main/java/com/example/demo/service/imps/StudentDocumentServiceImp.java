package com.example.demo.service.imps;

import com.example.demo.StudentDto.StudentDocumentDto;
import com.example.demo.entity.StudentDocumentEntity;
import com.example.demo.repository.StudentDocumentRepository;
import com.example.demo.service.IStudentDocumentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class StudentDocumentServiceImp implements IStudentDocumentService {
    private StudentDocumentRepository repository;

    private final ModelMapper mapper = new ModelMapper();
    @Override
    public Mono<StudentDocumentDto> saveStudent(StudentDocumentEntity student) {
        Mono<StudentDocumentEntity> savedStudent = repository.save(student);
        return savedStudent.map(studentEntity -> mapper.map(studentEntity, StudentDocumentDto.class));
    }
}
