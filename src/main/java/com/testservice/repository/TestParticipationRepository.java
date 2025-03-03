package com.testservice.repository;

import com.testservice.model.Student;
import com.testservice.model.Test;
import com.testservice.model.TestParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestParticipationRepository extends JpaRepository<TestParticipation, Long> {
    List<TestParticipation> findByStudent(Student student);
    List<TestParticipation> findByStudentId(Long studentId);
    List<TestParticipation> findByTest(Test test);
    List<TestParticipation> findByTestId(Long testId);
    Optional<TestParticipation> findByStudentAndTest(Student student, Test test);
    Optional<TestParticipation> findByStudentIdAndTestId(Long studentId, Long testId);
    boolean existsByStudentIdAndTestId(Long studentId, Long testId);
}
