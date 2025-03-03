package com.testservice.repository;

import com.testservice.model.Option;
import com.testservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByQuestion(Question question);
    List<Option> findByQuestionId(Long questionId);
}