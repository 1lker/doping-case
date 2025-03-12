package com.testservice.service;

import com.testservice.dto.request.StudentRequest;
import com.testservice.dto.response.StudentResponse;
import com.testservice.model.Student;
import com.testservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentRequest studentRequest;
    private List<Student> studentList;
    private Page<Student> studentPage;

    @BeforeEach
    void setUp() {
        // Set up test data
        student = new Student();
        student.setId(1L);
        student.setFirstName("Test");
        student.setLastName("Student");
        student.setStudentNumber("12345");
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        studentRequest = new StudentRequest();
        studentRequest.setFirstName("Test");
        studentRequest.setLastName("Student");
        studentRequest.setStudentNumber("12345");

        studentList = Arrays.asList(student);
        studentPage = new PageImpl<>(studentList);
    }

    @Test
    void getAllStudents() {
        when(studentRepository.findAll()).thenReturn(studentList);

        List<StudentResponse> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(student.getId(), result.get(0).getId());
        assertEquals(student.getFirstName(), result.get(0).getFirstName());
        assertEquals(student.getLastName(), result.get(0).getLastName());
        assertEquals(student.getStudentNumber(), result.get(0).getStudentNumber());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getAllStudentsPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        when(studentRepository.findAll(pageable)).thenReturn(studentPage);

        Page<StudentResponse> result = studentService.getAllStudentsPaged(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(student.getId(), result.getContent().get(0).getId());
        assertEquals(student.getFirstName(), result.getContent().get(0).getFirstName());
        assertEquals(student.getLastName(), result.getContent().get(0).getLastName());
        assertEquals(student.getStudentNumber(), result.getContent().get(0).getStudentNumber());

        verify(studentRepository, times(1)).findAll(pageable);
    }

    @Test
    void getStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponse result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        assertEquals(student.getFirstName(), result.getFirstName());
        assertEquals(student.getLastName(), result.getLastName());
        assertEquals(student.getStudentNumber(), result.getStudentNumber());

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void createStudent() {
        when(studentRepository.existsByStudentNumber(studentRequest.getStudentNumber())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponse result = studentService.createStudent(studentRequest);

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        assertEquals(student.getFirstName(), result.getFirstName());
        assertEquals(student.getLastName(), result.getLastName());
        assertEquals(student.getStudentNumber(), result.getStudentNumber());

        verify(studentRepository, times(1)).existsByStudentNumber(studentRequest.getStudentNumber());
        verify(studentRepository, times(1)).save(any(Student.class));
    }
} 