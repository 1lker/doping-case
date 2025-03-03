package com.testservice.dto.response;

public class AnswerResponse {
    private Long id;
    private Long questionId;
    private String questionContent;
    private Long selectedOptionId;
    private String selectedOptionContent;
    private String textAnswer;
    private Boolean isCorrect;
    private Integer pointsEarned;

    // Constructors
    public AnswerResponse() {
    }

    public AnswerResponse(Long id, Long questionId, String questionContent, Long selectedOptionId,
                          String selectedOptionContent, String textAnswer, Boolean isCorrect, Integer pointsEarned) {
        this.id = id;
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.selectedOptionId = selectedOptionId;
        this.selectedOptionContent = selectedOptionContent;
        this.textAnswer = textAnswer;
        this.isCorrect = isCorrect;
        this.pointsEarned = pointsEarned;
    }

    // Builder pattern implementation
    public static AnswerResponseBuilder builder() {
        return new AnswerResponseBuilder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public Long getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(Long selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }

    public String getSelectedOptionContent() {
        return selectedOptionContent;
    }

    public void setSelectedOptionContent(String selectedOptionContent) {
        this.selectedOptionContent = selectedOptionContent;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    // Builder class
    public static class AnswerResponseBuilder {
        private Long id;
        private Long questionId;
        private String questionContent;
        private Long selectedOptionId;
        private String selectedOptionContent;
        private String textAnswer;
        private Boolean isCorrect;
        private Integer pointsEarned;

        public AnswerResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AnswerResponseBuilder questionId(Long questionId) {
            this.questionId = questionId;
            return this;
        }

        public AnswerResponseBuilder questionContent(String questionContent) {
            this.questionContent = questionContent;
            return this;
        }

        public AnswerResponseBuilder selectedOptionId(Long selectedOptionId) {
            this.selectedOptionId = selectedOptionId;
            return this;
        }

        public AnswerResponseBuilder selectedOptionContent(String selectedOptionContent) {
            this.selectedOptionContent = selectedOptionContent;
            return this;
        }

        public AnswerResponseBuilder textAnswer(String textAnswer) {
            this.textAnswer = textAnswer;
            return this;
        }

        public AnswerResponseBuilder isCorrect(Boolean isCorrect) {
            this.isCorrect = isCorrect;
            return this;
        }

        public AnswerResponseBuilder pointsEarned(Integer pointsEarned) {
            this.pointsEarned = pointsEarned;
            return this;
        }

        public AnswerResponse build() {
            return new AnswerResponse(id, questionId, questionContent, selectedOptionId,
                    selectedOptionContent, textAnswer, isCorrect, pointsEarned);
        }
    }
}