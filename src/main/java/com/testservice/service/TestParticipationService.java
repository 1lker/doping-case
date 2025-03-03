package com.testservice.service;

import com.testservice.dto.request.AnswerRequest;
import com.testservice.dto.request.TestParticipationRequest;
import com.testservice.dto.response.AnswerResponse;
import com.testservice.dto.response.TestParticipationResponse;
import com.testservice.exception.BadRequestException;
import com.testservice.exception.ResourceNotFoundException;
import com.testservice.model.*;
import com.testservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestParticipationService {

    private final StudentRepository studentRepository;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final TestParticipationRepository participationRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public TestParticipationService(StudentRepository studentRepository,
                                    TestRepository testRepository,
                                    QuestionRepository questionRepository,
                                    OptionRepository optionRepository,
                                    TestParticipationRepository participationRepository,
                                    AnswerRepository answerRepository) {
        this.studentRepository = studentRepository;
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.participationRepository = participationRepository;
        this.answerRepository = answerRepository;
    }

    @Cacheable(value = "participations")
    public List<TestParticipationResponse> getAllParticipations() {
        return participationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "participationsByStudent", key = "#studentId")
    public List<TestParticipationResponse> getParticipationsByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Öğrenci bulunamadı. ID: " + studentId);
        }
        return participationRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "participationsByTest", key = "#testId")
    public List<TestParticipationResponse> getParticipationsByTestId(Long testId) {
        if (!testRepository.existsById(testId)) {
            throw new ResourceNotFoundException("Test bulunamadı. ID: " + testId);
        }
        return participationRepository.findByTestId(testId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "participation", key = "#id")
    public TestParticipationResponse getParticipationById(Long id) {
        TestParticipation participation = participationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test katılımı bulunamadı. ID: " + id));
        return mapToResponse(participation);
    }

    @Transactional
    public TestParticipationResponse startTest(TestParticipationRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı. ID: " + request.getStudentId()));

        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new ResourceNotFoundException("Test bulunamadı. ID: " + request.getTestId()));

        // Öğrencinin bu teste daha önce katılıp katılmadığını kontrol et
        if (participationRepository.existsByStudentIdAndTestId(request.getStudentId(), request.getTestId())) {
            throw new BadRequestException("Bu öğrenci zaten bu teste katıldı.");
        }

        TestParticipation participation = new TestParticipation();
        participation.setStudent(student);
        participation.setTest(test);
        participation.setStartTime(LocalDateTime.now());
        participation.setStatus(TestParticipation.ParticipationStatus.IN_PROGRESS);
        participation.setAnswers(new ArrayList<>());

        TestParticipation savedParticipation = participationRepository.save(participation);
        return mapToResponse(savedParticipation);
    }

    @Transactional
    @CacheEvict(value = {"participation", "participations", "participationsByStudent", "participationsByTest"}, allEntries = true)
    public TestParticipationResponse submitAnswer(AnswerRequest request) {
        TestParticipation participation = participationRepository.findById(request.getParticipationId())
                .orElseThrow(() -> new ResourceNotFoundException("Test katılımı bulunamadı. ID: " + request.getParticipationId()));

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Soru bulunamadı. ID: " + request.getQuestionId()));

        // Testin doğru teste ait olup olmadığını kontrol et
        if (!question.getTest().getId().equals(participation.getTest().getId())) {
            throw new BadRequestException("Bu soru bu teste ait değil.");
        }

        // Katılımın durumunu kontrol et
        if (participation.getStatus() != TestParticipation.ParticipationStatus.IN_PROGRESS) {
            throw new BadRequestException("Bu test zaten tamamlandı veya başlamadı.");
        }

        // Bu soruya daha önce cevap verildi mi kontrol et
        if (answerRepository.existsByParticipationIdAndQuestionId(request.getParticipationId(), request.getQuestionId())) {
            // Mevcut cevabı bul ve sil
            Answer existingAnswer = answerRepository.findByParticipationIdAndQuestionId(request.getParticipationId(), request.getQuestionId())
                    .orElseThrow();
            answerRepository.delete(existingAnswer);
        }

        Answer answer = new Answer();
        answer.setParticipation(participation);
        answer.setQuestion(question);

        boolean isCorrect = false;
        int pointsEarned = 0;

        // Cevap türüne göre işlem yap
        if (question.getQuestionType() == Question.QuestionType.MULTIPLE_CHOICE ||
                question.getQuestionType() == Question.QuestionType.TRUE_FALSE) {
            if (request.getSelectedOptionId() == null) {
                throw new BadRequestException("Çoktan seçmeli sorular için bir seçenek seçilmelidir.");
            }

            Option selectedOption = optionRepository.findById(request.getSelectedOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seçenek bulunamadı. ID: " + request.getSelectedOptionId()));

            // Seçeneğin bu soruya ait olup olmadığını kontrol et
            if (!selectedOption.getQuestion().getId().equals(question.getId())) {
                throw new BadRequestException("Bu seçenek bu soruya ait değil.");
            }

            answer.setSelectedOption(selectedOption);
            isCorrect = selectedOption.getIsCorrect();
            pointsEarned = isCorrect ? question.getPoints() : 0;
        } else if (question.getQuestionType() == Question.QuestionType.SHORT_ANSWER) {
            if (request.getTextAnswer() == null || request.getTextAnswer().trim().isEmpty()) {
                throw new BadRequestException("Kısa cevaplı sorular için bir cevap girilmelidir.");
            }

            answer.setTextAnswer(request.getTextAnswer());
            // Kısa cevaplı soruların değerlendirmesi manuel yapılacağı için doğruluk ve puan sonradan ayarlanacak
            isCorrect = false;
            pointsEarned = 0;
        }

        answer.setIsCorrect(isCorrect);
        answer.setPointsEarned(pointsEarned);

        Answer savedAnswer = answerRepository.save(answer);
        participation.getAnswers().add(savedAnswer);

        // Eğer tüm sorular cevaplandıysa, testi otomatik olarak tamamla
        if (participation.getAnswers().size() == questionRepository.findByTestId(participation.getTest().getId()).size()) {
            participation.setEndTime(LocalDateTime.now());
            participation.setStatus(TestParticipation.ParticipationStatus.COMPLETED);

            // Toplam puanı hesapla
            int totalScore = participation.getAnswers().stream()
                    .mapToInt(Answer::getPointsEarned)
                    .sum();
            participation.setScore(totalScore);
        }

        TestParticipation updatedParticipation = participationRepository.save(participation);
        return mapToResponse(updatedParticipation);
    }

    @Transactional
    @CacheEvict(value = {"participation", "participations", "participationsByStudent", "participationsByTest"}, allEntries = true)
    public TestParticipationResponse finishTest(Long participationId) {
        TestParticipation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ResourceNotFoundException("Test katılımı bulunamadı. ID: " + participationId));

        if (participation.getStatus() != TestParticipation.ParticipationStatus.IN_PROGRESS) {
            throw new BadRequestException("Bu test zaten tamamlandı veya başlamadı.");
        }

        participation.setEndTime(LocalDateTime.now());
        participation.setStatus(TestParticipation.ParticipationStatus.COMPLETED);

        // Toplam puanı hesapla
        int totalScore = participation.getAnswers().stream()
                .mapToInt(Answer::getPointsEarned)
                .sum();
        participation.setScore(totalScore);

        TestParticipation updatedParticipation = participationRepository.save(participation);
        return mapToResponse(updatedParticipation);
    }

    private TestParticipationResponse mapToResponse(TestParticipation participation) {
        List<AnswerResponse> answerResponses = participation.getAnswers().stream()
                .map(answer -> {
                    String selectedOptionContent = null;
                    if (answer.getSelectedOption() != null) {
                        selectedOptionContent = answer.getSelectedOption().getContent();
                    }

                    return AnswerResponse.builder()
                            .id(answer.getId())
                            .questionId(answer.getQuestion().getId())
                            .questionContent(answer.getQuestion().getContent())
                            .selectedOptionId(answer.getSelectedOption() != null ? answer.getSelectedOption().getId() : null)
                            .selectedOptionContent(selectedOptionContent)
                            .textAnswer(answer.getTextAnswer())
                            .isCorrect(answer.getIsCorrect())
                            .pointsEarned(answer.getPointsEarned())
                            .build();
                })
                .collect(Collectors.toList());

        return TestParticipationResponse.builder()
                .id(participation.getId())
                .studentId(participation.getStudent().getId())
                .studentName(participation.getStudent().getFirstName() + " " + participation.getStudent().getLastName())
                .testId(participation.getTest().getId())
                .testName(participation.getTest().getName())
                .startTime(participation.getStartTime())
                .endTime(participation.getEndTime())
                .status(participation.getStatus())
                .score(participation.getScore())
                .answers(answerResponses)
                .build();
    }
}