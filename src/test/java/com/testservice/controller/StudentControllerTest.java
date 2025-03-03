package com.testservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testservice.dto.request.StudentRequest;
import com.testservice.dto.response.StudentResponse;
import com.testservice.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentRequest studentRequest;
    private StudentResponse studentResponse;
    private List<StudentResponse> studentResponseList;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        studentRequest = new StudentRequest();
        studentRequest.setFirstName("Ali");
        studentRequest.setLastName("Yılmaz");
        studentRequest.setStudentNumber("123456");

        studentResponse = StudentResponse.builder()
                .id(1L)
                .firstName("Ali")
                .lastName("Yılmaz")
                .studentNumber("123456")
                .createdAt(now)
                .updatedAt(now)
                .build();

        StudentResponse studentResponse2 = StudentResponse.builder()
                .id(2L)
                .firstName("Ayşe")
                .lastName("Demir")
                .studentNumber("789012")
                .createdAt(now)
                .updatedAt(now)
                .build();

        studentResponseList = Arrays.asList(studentResponse, studentResponse2);
    }

    @Test
    void getAllStudents_ShouldReturnStudentList() throws Exception {
        when(studentService.getAllStudents()).thenReturn(studentResponseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getStudentById_ShouldReturnStudent() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getStudentByStudentNumber_ShouldReturnStudent() throws Exception {
        when(studentService.getStudentByStudentNumber("123456")).thenReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/number/123456"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createStudent_ShouldReturnCreatedStudent() throws Exception {
        when(studentService.createStudent(any(StudentRequest.class))).thenReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateStudent_ShouldReturnUpdatedStudent() throws Exception {
        when(studentService.updateStudent(eq(1L), any(StudentRequest.class))).thenReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteStudent_ShouldReturnNoContent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}