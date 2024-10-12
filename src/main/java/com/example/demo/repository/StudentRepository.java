package com.example.demo.repository;

import com.example.demo.StudentDto.StudentDto;
import com.example.demo.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findStudentByEmail(String studentEmail);

    @Query("select std from StudentEntity std where std.firstName =?1 and std.lastName = ?2")
    StudentEntity findByJPQL(String firstName, String lastName);

    @Query("select std from StudentEntity std where std.firstName = :firstName and std.lastName = :lastName")
    StudentEntity findByJPQLParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(value = "select std from students std where std.first_name =?1 and std.last_name =?1", nativeQuery = true)
    StudentEntity findByJPQLNativeQuery(String firstName, String lastName);

    @Query(value = "select std from students std where std.first_name =:firstName and std.last_name =:lastName", nativeQuery = true)
    StudentEntity findByJPQLParamsNativeQuery(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
