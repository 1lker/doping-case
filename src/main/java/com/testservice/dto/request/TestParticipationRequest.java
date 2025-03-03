package com.testservice.dto.request;

import jakarta.validation.constraints.NotNull;

public class TestParticipationRequest {
    @NotNull(message = "Öğrenci ID belirtilmelidir")
    private Long studentId;

    @NotNull(message = "Test ID belirtilmelidir")
    private Long testId;

    // Constructors
    public TestParticipationRequest() {
    }

    public TestParticipationRequest(Long studentId, Long testId) {
        this.studentId = studentId;
        this.testId = testId;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }
}