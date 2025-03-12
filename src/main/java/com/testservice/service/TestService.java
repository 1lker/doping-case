package com.testservice.service;

import com.testservice.dto.request.OptionRequest;
import com.testservice.dto.request.QuestionRequest;
import com.testservice.dto.request.TestRequest;
import com.testservice.dto.response.OptionResponse;
import com.testservice.dto.response.QuestionResponse;
import com.testservice.dto.response.TestResponse;
import com.testservice.exception.BadRequestException;
import com.testservice.exception.ResourceNotFoundException;
import com.testservice.model.Option;
import com.testservice.model.Question;
import com.testservice.model.Test;
import com.testservice.repository.OptionRepository;
import com.testservice.repository.QuestionRepository;
import com.testservice.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public TestService(TestRepository testRepository, QuestionRepository questionRepository, OptionRepository optionRepository) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    @Cacheable("tests")
    public List<TestResponse> getAllTests() {
        return testRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "testsPage", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<TestResponse> getAllTestsPaged(Pageable pageable) {
        return testRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Cacheable(value = "test", key = "#id")
    public TestResponse getTestById(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test bulunamadı. ID: " + id));
        return mapToResponse(test);
    }

    @Transactional
    public TestResponse createTest(TestRequest request) {
        if (testRepository.existsByName(request.getName())) {
            throw new BadRequestException("Bu test adı zaten kullanılıyor: " + request.getName());
        }

        // Test oluştur
        Test test = new Test();
        test.setName(request.getName());
        test.setDescription(request.getDescription());
        test.setDurationMinutes(request.getDurationMinutes());
        test.setQuestions(new ArrayList<>());

        Test savedTest = testRepository.save(test);

        // Soruları ekle
        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            for (QuestionRequest questionRequest : request.getQuestions()) {
                Question question = createQuestion(savedTest, questionRequest);
                savedTest.getQuestions().add(question);
            }
        }

        return mapToResponse(savedTest);
    }

    @Transactional
    @CacheEvict(value = {"test", "tests"}, allEntries = true)
    public TestResponse updateTest(Long id, TestRequest request) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test bulunamadı. ID: " + id));

        // Eğer test adı değiştiyse ve yeni ad başka bir test tarafından kullanılıyorsa hata fırlat
        if (!test.getName().equals(request.getName()) &&
                testRepository.existsByName(request.getName())) {
            throw new BadRequestException("Bu test adı zaten kullanılıyor: " + request.getName());
        }

        test.setName(request.getName());
        test.setDescription(request.getDescription());
        test.setDurationMinutes(request.getDurationMinutes());

        // Mevcut soruları sil
        questionRepository.deleteAll(test.getQuestions());
        test.getQuestions().clear();

        // Yeni soruları ekle
        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            for (QuestionRequest questionRequest : request.getQuestions()) {
                Question question = createQuestion(test, questionRequest);
                test.getQuestions().add(question);
            }
        }

        Test updatedTest = testRepository.save(test);
        return mapToResponse(updatedTest);
    }

    @Transactional
    @CacheEvict(value = {"test", "tests"}, allEntries = true)
    public void deleteTest(Long id) {
        if (!testRepository.existsById(id)) {
            throw new ResourceNotFoundException("Test bulunamadı. ID: " + id);
        }
        testRepository.deleteById(id);
    }

    private Question createQuestion(Test test, QuestionRequest questionRequest) {
        Question question = new Question();
        question.setTest(test);
        question.setContent(questionRequest.getContent());
        question.setQuestionType(questionRequest.getQuestionType());
        question.setPoints(questionRequest.getPoints());
        question.setOptions(new ArrayList<>());

        Question savedQuestion = questionRepository.save(question);

        // Seçenekleri ekle
        if (questionRequest.getOptions() != null && !questionRequest.getOptions().isEmpty()) {
            for (OptionRequest optionRequest : questionRequest.getOptions()) {
                Option option = new Option();
                option.setQuestion(savedQuestion);
                option.setContent(optionRequest.getContent());
                option.setIsCorrect(optionRequest.getIsCorrect());

                Option savedOption = optionRepository.save(option);
                savedQuestion.getOptions().add(savedOption);
            }
        }

        return savedQuestion;
    }

    private TestResponse mapToResponse(Test test) {
        List<QuestionResponse> questionResponses = test.getQuestions().stream()
                .map(question -> {
                    List<OptionResponse> optionResponses = question.getOptions().stream()
                            .map(option -> OptionResponse.builder()
                                    .id(option.getId())
                                    .content(option.getContent())
                                    .isCorrect(option.getIsCorrect())
                                    .build())
                            .collect(Collectors.toList());

                    return QuestionResponse.builder()
                            .id(question.getId())
                            .content(question.getContent())
                            .questionType(question.getQuestionType())
                            .points(question.getPoints())
                            .options(optionResponses)
                            .build();
                })
                .collect(Collectors.toList());

        return TestResponse.builder()
                .id(test.getId())
                .name(test.getName())
                .description(test.getDescription())
                .durationMinutes(test.getDurationMinutes())
                .questions(questionResponses)
                .createdAt(test.getCreatedAt())
                .updatedAt(test.getUpdatedAt())
                .build();
    }
}