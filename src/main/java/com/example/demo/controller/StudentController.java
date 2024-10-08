package com.example.demo.controller;

import com.example.demo.StudentDto.StudentDto;
import com.example.demo.entity.StudentEntity;
import com.example.demo.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    @GetMapping("create-student")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDto createStudent(@RequestBody StudentDto student){
        return this.studentService.createStudent(student);
    }

    @GetMapping("get-all-student")
    public Page<StudentEntity> getAllStudent(@RequestParam Pageable pageable){
        return this.studentService.getAllStudent(pageable);
    }

    @GetMapping("get-student/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") long studentId){
        StudentDto student = this.studentService.getStudentById(studentId);
        if(Objects.isNull(student)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(student);
    }
   @PatchMapping("update-student/{id}")
    public ResponseEntity<StudentDto> updateStudentById(@PathVariable("id") long studentId,
                                                        @RequestBody StudentDto student){
        StudentDto studentDto = this.studentService.updateStudent(studentId, student);
       if(Objects.isNull(studentDto)){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
       }
       return ResponseEntity.ok(studentDto);
   }
   @DeleteMapping("remove-student/{id}")
   public ResponseEntity removeStudentById(@PathVariable("id") long studentId){
       boolean result = this.studentService.deleteStudent(studentId);
       if(!result){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
       }
       return ResponseEntity.ok().build();
   }
}
