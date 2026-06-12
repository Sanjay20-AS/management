package com.employee.management.controller;

import com.employee.management.dto.StudentRequest;
import com.employee.management.dto.StudentResponse;
import com.employee.management.service.StudentService;
import com.employee.management.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/students")
@RequiredArgsConstructor

public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getstudents(){

        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<String> addStudents(@RequestBody Student s){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.addAllStudents(s));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudents(@PathVariable int id, @RequestBody StudentRequest s){

        return ResponseEntity.ok(studentService.updateAllStudents(id, s));

    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable long id){
        //return studentService.getStu(id);

        StudentResponse response= studentService.getStu(id);
        if(response==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody Student s){

        //return studentService.updateStu(id, s);
        Student student = studentService.updateStu(id,s);
        if(student==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok("Student updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id){


        return ResponseEntity.ok(studentService.deleteStu(id));
    }
}
