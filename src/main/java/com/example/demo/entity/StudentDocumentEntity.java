package com.example.demo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "students")
public class StudentDocumentEntity {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
}
