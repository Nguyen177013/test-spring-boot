package com.example.demo.service.imps;

import com.example.demo.StudentDto.StudentDto;
import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.IStudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class StudentServiceImp implements IStudentService {
    @Autowired
    StudentRepository repository;

    @Override
    public StudentDto createStudent(StudentDto student) {
        if(Objects.isNull(student.getEmail())){
            throw new NullPointerException("missing student email");
        }
        Optional<StudentDto> newStudent = repository.findStudentByEmail(student.getEmail());
        if(newStudent.isPresent()){
            throw new DuplicateKeyException("student email already exists");
        }
        ModelMapper modelMapper = new ModelMapper();
        repository.save(modelMapper.map(student, StudentEntity.class));
        return newStudent.get();
    }

    @Override
    public Page<StudentEntity> getAllStudent(Pageable pageable) {
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
    public StudentDto updateStudent(StudentDto student) {
        if(Objects.isNull(this.getStudentById(student.getId()))){
            throw new NullPointerException("student id does not exist");
        }
        ModelMapper modelMapper = new ModelMapper();
        repository.save(modelMapper.map(student, StudentEntity.class));
        return student;
    }

    @Override
    public void deleteStudent(long studentId) {
        this.repository.deleteById(studentId);
    }
}
