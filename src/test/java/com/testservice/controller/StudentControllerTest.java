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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
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
    private Page<StudentResponse> studentResponsePage;

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
        studentResponsePage = new PageImpl<>(studentResponseList);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void getAllStudents_ShouldReturnStudentList() throws Exception {
        // For controller that returns Page directly, we need to mock getAllStudentsPaged
        when(studentService.getAllStudentsPaged(any(Pageable.class))).thenReturn(studentResponsePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void getStudentById_ShouldReturnStudent() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void getStudentByStudentNumber_ShouldReturnStudent() throws Exception {
        when(studentService.getStudentByStudentNumber("123456")).thenReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/number/123456")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void createStudent_ShouldReturnCreatedStudent() throws Exception {
        when(studentService.createStudent(any(StudentRequest.class))).thenReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void updateStudent_ShouldReturnUpdatedStudent() throws Exception {
        when(studentService.updateStudent(eq(1L), any(StudentRequest.class))).thenReturn(studentResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStudent_ShouldReturnNoContent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testGetAllStudentsWithPagination() throws Exception {
        // Set up paginated students
        List<StudentResponse> pagedStudents = Arrays.asList(studentResponse);
        Page<StudentResponse> studentPage = new PageImpl<>(pagedStudents);
        
        when(studentService.getAllStudentsPaged(any(Pageable.class))).thenReturn(studentPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(studentResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testGetAllStudentsWithoutPagination() throws Exception {
        when(studentService.getAllStudents()).thenReturn(studentResponseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/all")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(studentResponse.getId()));
    }
}