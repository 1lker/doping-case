package com.testservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class TestResponse {
    private Long id;
    private String name;
    private String description;
    private Integer durationMinutes;
    private List<QuestionResponse> questions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public TestResponse() {
    }

    public TestResponse(Long id, String name, String description, Integer durationMinutes,
                        List<QuestionResponse> questions, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.questions = questions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Builder pattern implementation
    public static TestResponseBuilder builder() {
        return new TestResponseBuilder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Builder class
    public static class TestResponseBuilder {
        private Long id;
        private String name;
        private String description;
        private Integer durationMinutes;
        private List<QuestionResponse> questions;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public TestResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TestResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TestResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TestResponseBuilder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }

        public TestResponseBuilder questions(List<QuestionResponse> questions) {
            this.questions = questions;
            return this;
        }

        public TestResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TestResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public TestResponse build() {
            return new TestResponse(id, name, description, durationMinutes, questions, createdAt, updatedAt);
        }
    }
}