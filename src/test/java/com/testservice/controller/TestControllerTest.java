package com.testservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testservice.dto.request.OptionRequest;
import com.testservice.dto.request.QuestionRequest;
import com.testservice.dto.request.TestRequest;
import com.testservice.dto.response.TestResponse;
import com.testservice.model.Question;
import com.testservice.service.TestService;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestService testService;

    @Autowired
    private ObjectMapper objectMapper;

    private TestRequest testRequest;
    private TestResponse testResponse;
    private List<TestResponse> testResponseList;
    private Page<TestResponse> testResponsePage;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        // Create option requests
        OptionRequest optionRequest1 = new OptionRequest();
        optionRequest1.setContent("Option 1");
        optionRequest1.setIsCorrect(true);

        OptionRequest optionRequest2 = new OptionRequest();
        optionRequest2.setContent("Option 2");
        optionRequest2.setIsCorrect(false);

        // Create question request
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setContent("What is 2+2?");
        questionRequest.setQuestionType(Question.QuestionType.MULTIPLE_CHOICE);
        questionRequest.setPoints(10);
        questionRequest.setOptions(Arrays.asList(optionRequest1, optionRequest2));

        // Set up test request
        testRequest = new TestRequest();
        testRequest.setName("Math Exam");
        testRequest.setDescription("Mid-term math exam");
        testRequest.setDurationMinutes(60);
        testRequest.setQuestions(Collections.singletonList(questionRequest));

        testResponse = TestResponse.builder()
                .id(1L)
                .name("Math Exam")
                .description("Mid-term math exam")
                .durationMinutes(60)
                .createdAt(now)
                .updatedAt(now)
                .build();

        TestResponse testResponse2 = TestResponse.builder()
                .id(2L)
                .name("Physics Exam")
                .description("Final physics exam")
                .durationMinutes(90)
                .createdAt(now)
                .updatedAt(now)
                .build();

        testResponseList = Arrays.asList(testResponse, testResponse2);
        testResponsePage = new PageImpl<>(testResponseList);
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getAllTests_ShouldReturnTestList() throws Exception {
        when(testService.getAllTestsPaged(any(Pageable.class))).thenReturn(testResponsePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tests")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getTestById_ShouldReturnTest() throws Exception {
        when(testService.getTestById(1L)).thenReturn(testResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void createTest_ShouldReturnCreatedTest() throws Exception {
        when(testService.createTest(any(TestRequest.class))).thenReturn(testResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void updateTest_ShouldReturnUpdatedTest() throws Exception {
        when(testService.updateTest(eq(1L), any(TestRequest.class))).thenReturn(testResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTest_ShouldReturnNoContent() throws Exception {
        doNothing().when(testService).deleteTest(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tests/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testGetAllTestsWithPagination() throws Exception {
        // Set up paginated tests
        List<TestResponse> pagedTests = Arrays.asList(testResponse);
        Page<TestResponse> testPage = new PageImpl<>(pagedTests);
        
        when(testService.getAllTestsPaged(any(Pageable.class))).thenReturn(testPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tests")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testGetAllTestsWithoutPagination() throws Exception {
        when(testService.getAllTests()).thenReturn(testResponseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tests/all")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(testResponse.getId()));
    }
} 