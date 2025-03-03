package com.testservice.dto.request;

import jakarta.validation.constraints.NotNull;

public class AnswerRequest {
    @NotNull(message = "Katılım ID belirtilmelidir")
    private Long participationId;

    @NotNull(message = "Soru ID belirtilmelidir")
    private Long questionId;

    private Long selectedOptionId;

    private String textAnswer;

    // Constructors
    public AnswerRequest() {
    }

    public AnswerRequest(Long participationId, Long questionId, Long selectedOptionId, String textAnswer) {
        this.participationId = participationId;
        this.questionId = questionId;
        this.selectedOptionId = selectedOptionId;
        this.textAnswer = textAnswer;
    }

    // Getters and Setters
    public Long getParticipationId() {
        return participationId;
    }

    public void setParticipationId(Long participationId) {
        this.participationId = participationId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(Long selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }
}