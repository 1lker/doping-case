package com.testservice.dto.response;

import java.time.LocalDateTime;

public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String studentNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public StudentResponse() {
    }

    public StudentResponse(Long id, String firstName, String lastName, String studentNumber,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentNumber = studentNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Builder pattern implementation for StudentResponse
    public static StudentResponseBuilder builder() {
        return new StudentResponseBuilder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
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

    // Builder class for StudentResponse
    public static class StudentResponseBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String studentNumber;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public StudentResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public StudentResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentResponseBuilder studentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
            return this;
        }

        public StudentResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StudentResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public StudentResponse build() {
            return new StudentResponse(id, firstName, lastName, studentNumber, createdAt, updatedAt);
        }
    }
}