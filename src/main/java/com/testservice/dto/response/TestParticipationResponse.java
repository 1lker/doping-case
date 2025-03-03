package com.testservice.dto.response;

import com.testservice.model.TestParticipation;
import java.time.LocalDateTime;
import java.util.List;

public class TestParticipationResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long testId;
    private String testName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TestParticipation.ParticipationStatus status;
    private Integer score;
    private List<AnswerResponse> answers;

    // Constructors
    public TestParticipationResponse() {
    }

    public TestParticipationResponse(Long id, Long studentId, String studentName, Long testId, String testName,
                                     LocalDateTime startTime, LocalDateTime endTime,
                                     TestParticipation.ParticipationStatus status, Integer score,
                                     List<AnswerResponse> answers) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.testId = testId;
        this.testName = testName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.score = score;
        this.answers = answers;
    }

    // Builder pattern implementation
    public static TestParticipationResponseBuilder builder() {
        return new TestParticipationResponseBuilder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public TestParticipation.ParticipationStatus getStatus() {
        return status;
    }

    public void setStatus(TestParticipation.ParticipationStatus status) {
        this.status = status;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<AnswerResponse> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerResponse> answers) {
        this.answers = answers;
    }

    // Builder class
    public static class TestParticipationResponseBuilder {
        private Long id;
        private Long studentId;
        private String studentName;
        private Long testId;
        private String testName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private TestParticipation.ParticipationStatus status;
        private Integer score;
        private List<AnswerResponse> answers;

        public TestParticipationResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TestParticipationResponseBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public TestParticipationResponseBuilder studentName(String studentName) {
            this.studentName = studentName;
            return this;
        }

        public TestParticipationResponseBuilder testId(Long testId) {
            this.testId = testId;
            return this;
        }

        public TestParticipationResponseBuilder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public TestParticipationResponseBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public TestParticipationResponseBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public TestParticipationResponseBuilder status(TestParticipation.ParticipationStatus status) {
            this.status = status;
            return this;
        }

        public TestParticipationResponseBuilder score(Integer score) {
            this.score = score;
            return this;
        }

        public TestParticipationResponseBuilder answers(List<AnswerResponse> answers) {
            this.answers = answers;
            return this;
        }

        public TestParticipationResponse build() {
            return new TestParticipationResponse(id, studentId, studentName, testId, testName,
                    startTime, endTime, status, score, answers);
        }
    }
}