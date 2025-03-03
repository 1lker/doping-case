package com.testservice.repository;

import com.testservice.model.Answer;
import com.testservice.model.Question;
import com.testservice.model.TestParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByParticipation(TestParticipation participation);
    List<Answer> findByParticipationId(Long participationId);
    Optional<Answer> findByParticipationAndQuestion(TestParticipation participation, Question question);
    Optional<Answer> findByParticipationIdAndQuestionId(Long participationId, Long questionId);
    boolean existsByParticipationIdAndQuestionId(Long participationId, Long questionId);
}