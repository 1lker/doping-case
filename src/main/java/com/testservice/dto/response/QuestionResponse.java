package com.testservice.dto.response;

import com.testservice.model.Question;
import java.util.List;

public class QuestionResponse {
    private Long id;
    private String content;
    private Question.QuestionType questionType;
    private Integer points;
    private List<OptionResponse> options;

    // Constructors
    public QuestionResponse() {
    }

    public QuestionResponse(Long id, String content, Question.QuestionType questionType,
                            Integer points, List<OptionResponse> options) {
        this.id = id;
        this.content = content;
        this.questionType = questionType;
        this.points = points;
        this.options = options;
    }

    // Builder pattern implementation
    public static QuestionResponseBuilder builder() {
        return new QuestionResponseBuilder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<OptionResponse> getOptions() {
        return options;
    }

    public void setOptions(List<OptionResponse> options) {
        this.options = options;
    }

    // Builder class
    public static class QuestionResponseBuilder {
        private Long id;
        private String content;
        private Question.QuestionType questionType;
        private Integer points;
        private List<OptionResponse> options;

        public QuestionResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public QuestionResponseBuilder content(String content) {
            this.content = content;
            return this;
        }

        public QuestionResponseBuilder questionType(Question.QuestionType questionType) {
            this.questionType = questionType;
            return this;
        }

        public QuestionResponseBuilder points(Integer points) {
            this.points = points;
            return this;
        }

        public QuestionResponseBuilder options(List<OptionResponse> options) {
            this.options = options;
            return this;
        }

        public QuestionResponse build() {
            return new QuestionResponse(id, content, questionType, points, options);
        }
    }
}