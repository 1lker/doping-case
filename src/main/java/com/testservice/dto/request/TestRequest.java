package com.testservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public class TestRequest {
    @NotBlank(message = "Test adı boş olamaz")
    @Size(max = 100, message = "Test adı en fazla 100 karakter olabilir")
    private String name;

    @Size(max = 500, message = "Açıklama en fazla 500 karakter olabilir")
    private String description;

    @Positive(message = "Süre pozitif bir değer olmalıdır")
    private Integer durationMinutes;

    @NotEmpty(message = "En az bir soru eklemelisiniz")
    @Valid
    private List<QuestionRequest> questions;

    // Constructors
    public TestRequest() {
    }

    public TestRequest(String name, String description, Integer durationMinutes, List<QuestionRequest> questions) {
        this.name = name;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.questions = questions;
    }

    // Getters and Setters
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

    public List<QuestionRequest> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionRequest> questions) {
        this.questions = questions;
    }
}