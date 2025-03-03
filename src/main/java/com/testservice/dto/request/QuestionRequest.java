package com.testservice.dto.request;

import com.testservice.model.Question;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class QuestionRequest {
    @NotBlank(message = "Soru içeriği boş olamaz")
    private String content;

    @NotNull(message = "Soru tipi belirtilmelidir")
    private Question.QuestionType questionType;

    @Positive(message = "Puan değeri pozitif olmalıdır")
    private Integer points;

    @Valid
    private List<OptionRequest> options;

    // Constructors
    public QuestionRequest() {
    }

    public QuestionRequest(String content, Question.QuestionType questionType, Integer points, List<OptionRequest> options) {
        this.content = content;
        this.questionType = questionType;
        this.points = points;
        this.options = options;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Question.QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Question.QuestionType questionType) {
        this.questionType = questionType;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<OptionRequest> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRequest> options) {
        this.options = options;
    }
}