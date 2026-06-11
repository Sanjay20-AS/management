package com.employee.management.service;

import com.employee.management.dto.AddressDTO;
import com.employee.management.dto.StudentResponse;
import com.employee.management.model.Student;
import com.employee.management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;


    public List<StudentResponse> getAllStudents(){

        return studentRepository.findAll().stream().map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public String addAllStudents( Student s){
        studentRepository.save(s);
        return "Students added successfully";
    }

    public String updateAllStudents(long id, Student s){
        Student student= studentRepository.findById(id).orElse(null);
        if(student==null){
            return "Student not updated";
        }
        student.setFirstName(s.getFirstName());
        student.setLastName(s.getLastName());
        studentRepository.save(student);
        return "Student updated successfully";

    }

    public Student getStu(Long id){

        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStu(Long id, Student s){

        /*  Optional<Student> isPresent= students.stream().
                filter(st-> st.getId()==id).findFirst();
        if(isPresent.isPresent()){
            Student student=isPresent.get();
            student.setFirstName(s.getFirstName());
            student.setLastName(s.getLastName());
            return "Student updated successfully";
        }
        return "Student not found"; */

        Optional<Student> existingStudent = studentRepository.findById(id);

        if(existingStudent.isPresent()){
            Student student=existingStudent.get();
            student.setFirstName(s.getFirstName());
            student.setLastName(s.getLastName());
            student.setPhone(s.getPhone());
            student.setEmail(s.getEmail());
            student.setAddress(s.getAddress());
            return studentRepository.save(student);
        }
        return null;

    }

    public String deleteStu( long id){
        if(studentRepository.existsById(id)){
            studentRepository.deleteById(id);
            return "Student deleted successfully";
        }
        return "Student not found";
    }

    private StudentResponse mapToUserResponse(Student student){
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setPhone(student.getPhone());
        response.setEmail(student.getEmail());
        response.setRole(student.getRole());

        if(student.getAddress()!=null){
            AddressDTO addressDTO= new AddressDTO();
            addressDTO.setStreet(student.getAddress().getStreet());
            addressDTO.setCity(student.getAddress().getCity());
            addressDTO.setState(student.getAddress().getState());
            addressDTO.setCountry(student.getAddress().getCountry());
            addressDTO.setZipcode(student.getAddress().getZipcode());
            response.setAddress(addressDTO);
        }

        return  response;

    }

}
