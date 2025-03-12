package com.testservice.service;

import com.testservice.model.Student;
import com.testservice.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PaginationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CacheManager cacheManager;

    private List<Student> testStudents = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // Clear the cache before each test
        Objects.requireNonNull(cacheManager.getCache("studentsPage")).clear();
        Objects.requireNonNull(cacheManager.getCache("students")).clear();
        
        // Create 25 test students
        for (int i = 1; i <= 25; i++) {
            Student student = new Student();
            student.setFirstName("Test" + i);
            student.setLastName("Student" + i);
            student.setStudentNumber("STD" + (10000 + i));
            testStudents.add(studentRepository.save(student));
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up test data
        studentRepository.deleteAll(testStudents);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testDefaultPagination() throws Exception {
        // Test default pagination (page 0, size 10)
        mockMvc.perform(get("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(10)))
                .andExpect(jsonPath("$.totalElements", is(25)))
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testCustomPagination() throws Exception {
        // Test custom pagination (page 1, size 5)
        mockMvc.perform(get("/api/students")
                .param("page", "1")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(5)))
                .andExpect(jsonPath("$.totalElements", is(25)))
                .andExpect(jsonPath("$.totalPages", is(5)))
                .andExpect(jsonPath("$.number", is(1)));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testLastPage() throws Exception {
        // Test last page (page 2, size 10)
        mockMvc.perform(get("/api/students")
                .param("page", "2")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(5)))
                .andExpect(jsonPath("$.totalElements", is(25)))
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.number", is(2)))
                .andExpect(jsonPath("$.last", is(true)));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testNoPagination() throws Exception {
        // Test endpoint without pagination
        mockMvc.perform(get("/api/students/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(25)));
    }
} 