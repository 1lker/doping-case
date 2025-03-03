package com.testservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OptionRequest {
    @NotBlank(message = "Seçenek içeriği boş olamaz")
    private String content;

    @NotNull(message = "Doğruluk değeri belirtilmelidir")
    private Boolean isCorrect;

    // Constructors
    public OptionRequest() {
    }

    public OptionRequest(String content, Boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}