package com.example.demo.StudentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDocumentDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}
