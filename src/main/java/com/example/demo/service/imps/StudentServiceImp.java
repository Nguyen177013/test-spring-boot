package com.example.demo.service.imps;

import com.example.demo.StudentDto.StudentDto;
import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class StudentServiceImp implements IStudentService {
    @Autowired
    StudentRepository repository;

    @Override
    public StudentDto createStudent(StudentEntity student) {
        if(Objects.isNull(student.getEmail())){
            throw new NullPointerException("missing student email");
        }
        Optional<StudentEntity> existStudent = repository.findStudentByEmail(student.getEmail());
        if(existStudent.isPresent()){
            throw new DuplicateKeyException("student email already exists");
        }
        ModelMapper modelMapper = new ModelMapper();
        var newStudent = repository.save(modelMapper.map(student, StudentEntity.class));
        return modelMapper.map(newStudent, StudentDto.class);
    }

    @Override
    public Page<StudentEntity> getAllStudent(Pageable pageable) {
        var a = this.repository.findAll();
        return this.repository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    @Override
    public StudentDto getStudentById(long studentId) {
        Optional<StudentEntity> entity = repository.findById(studentId);
        if(entity.isEmpty()){
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity.get(), StudentDto.class);
    }

    @Override
    public StudentDto updateStudent(long studentId, StudentEntity student) {
        if(Objects.isNull(this.getStudentById(studentId))){
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        repository.save(student);
        return modelMapper.map(student, StudentDto.class);
    }

    @Override
    public boolean deleteStudent(long studentId) {
        try{
            this.repository.deleteById(studentId);
            return true;
        } catch(Exception ex){
            log.error(ex.getMessage());
            return false;
        }
    }
}
