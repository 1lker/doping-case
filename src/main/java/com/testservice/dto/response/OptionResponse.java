package com.testservice.dto.response;

public class OptionResponse {
    private Long id;
    private String content;
    private Boolean isCorrect;

    // Constructors
    public OptionResponse() {
    }

    public OptionResponse(Long id, String content, Boolean isCorrect) {
        this.id = id;
        this.content = content;
        this.isCorrect = isCorrect;
    }

    // Builder pattern implementation
    public static OptionResponseBuilder builder() {
        return new OptionResponseBuilder();
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

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    // Builder class
    public static class OptionResponseBuilder {
        private Long id;
        private String content;
        private Boolean isCorrect;

        public OptionResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OptionResponseBuilder content(String content) {
            this.content = content;
            return this;
        }

        public OptionResponseBuilder isCorrect(Boolean isCorrect) {
            this.isCorrect = isCorrect;
            return this;
        }

        public OptionResponse build() {
            return new OptionResponse(id, content, isCorrect);
        }
    }
}