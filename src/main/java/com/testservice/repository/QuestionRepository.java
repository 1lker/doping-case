package com.testservice.repository;

import com.testservice.model.Question;
import com.testservice.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTest(Test test);
    List<Question> findByTestId(Long testId);
}