package com.testservice.config;

import com.testservice.model.*;
import com.testservice.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class DataPopulator {
    private static final Logger logger = LoggerFactory.getLogger(DataPopulator.class);
    
    // Veri boyutları
    private static final int STUDENT_COUNT = 10000;
    private static final int TEST_COUNT = 100;
    private static final int QUESTIONS_PER_TEST = 20;
    private static final int OPTIONS_PER_QUESTION = 4;
    private static final int PARTICIPATIONS_PER_STUDENT = 5;  // Her öğrenci 5 teste katılır
    
    // İsim listeleri
    private static final String[] FIRST_NAMES = {
            "Ahmet", "Mehmet", "Ali", "Ayşe", "Fatma", "Zeynep", "Mustafa", "Emine", "Hüseyin", "Hatice",
            "İbrahim", "Meryem", "Ömer", "Elif", "Yusuf", "Zehra", "Hasan", "Hacer", "Hüsnü", "Sultan"
    };
    
    private static final String[] LAST_NAMES = {
            "Yılmaz", "Kaya", "Demir", "Çelik", "Şahin", "Yıldız", "Yıldırım", "Öztürk", "Aydın", "Özdemir",
            "Arslan", "Doğan", "Kılıç", "Aslan", "Çetin", "Kara", "Kurt", "Koç", "Özkan", "Şimşek"
    };
    
    private static final String[] TEST_SUBJECTS = {
            "Matematik", "Fizik", "Kimya", "Biyoloji", "Tarih", "Coğrafya", "Edebiyat", "İngilizce",
            "Almanca", "Fransızca", "Felsefe", "Sosyoloji", "Psikoloji", "Ekonomi", "İstatistik"
    };
    
    private static final String[] QUESTION_TEMPLATES = {
            "%s konusunda hangi ifade doğrudur?",
            "%s için aşağıdaki tanımlardan hangisi yanlıştır?",
            "Aşağıdakilerden hangisi %s örneğidir?",
            "%s ile ilgili verilen bilgilerden hangisi doğrudur?",
            "%s alanında çalışma yapan bilim insanı kimdir?",
            "%s yöntemlerinden hangisi daha verimlidir?",
            "%s'nin tarihsel gelişiminde hangi olay daha önemlidir?",
            "%s uygulamasında kritik nokta nedir?",
            "%s teorisini kim ortaya atmıştır?",
            "Aşağıdakilerden hangisi %s formülüdür?"
    };
    
    @Bean
    @Profile("!test")  // Test profilinde çalıştırılmasın
    public CommandLineRunner initDatabase(
            StudentRepository studentRepository,
            TestRepository testRepository,
            QuestionRepository questionRepository,
            OptionRepository optionRepository,
            TestParticipationRepository participationRepository,
            AnswerRepository answerRepository) {
        
        return args -> {
            if (studentRepository.count() > 0) {
                logger.info("Veritabanı zaten dolu, veri ekleme işlemi atlanıyor...");
                return;
            }
            
            long startTime = System.currentTimeMillis();
            logger.info("Veritabanı doldurma işlemi başlatılıyor...");
            
            // 1. Öğrencileri Ekle
            List<Student> students = createStudents(studentRepository);
            logger.info("{} öğrenci eklendi", students.size());
            
            // 2. Testleri, Soruları ve Seçenekleri Ekle
            List<Test> tests = createTests(testRepository, questionRepository, optionRepository);
            logger.info("{} test eklendi", tests.size());
            
            // 3. Test Katılımları ve Cevapları Ekle
            createParticipationsAndAnswers(students, tests, participationRepository, answerRepository);
            
            long endTime = System.currentTimeMillis();
            logger.info("Veri tabanı doldurma işlemi tamamlandı. Geçen süre: {} saniye", 
                    (endTime - startTime) / 1000);
        };
    }
    
    @Transactional
    protected List<Student> createStudents(StudentRepository studentRepository) {
        List<Student> students = new ArrayList<>();
        
        for (int i = 0; i < STUDENT_COUNT; i++) {
            Student student = new Student();
            student.setFirstName(FIRST_NAMES[ThreadLocalRandom.current().nextInt(FIRST_NAMES.length)]);
            student.setLastName(LAST_NAMES[ThreadLocalRandom.current().nextInt(LAST_NAMES.length)]);
            student.setStudentNumber(String.format("%08d", i + 1)); // 8 haneli öğrenci numarası
            students.add(student);
            
            // Her 1000 öğrencide bir batch insert yaparak performansı artır
            if (i % 1000 == 999 || i == STUDENT_COUNT - 1) {
                studentRepository.saveAll(students);
                logger.info("{} öğrenci eklendi", i + 1);
                students.clear();
            }
        }
        
        return studentRepository.findAll();
    }
    
    @Transactional
    protected List<Test> createTests(
            TestRepository testRepository,
            QuestionRepository questionRepository,
            OptionRepository optionRepository) {
        
        List<Test> allTests = new ArrayList<>();
        
        for (int i = 0; i < TEST_COUNT; i++) {
            // Test oluştur
            Test test = new Test();
            String subject = TEST_SUBJECTS[i % TEST_SUBJECTS.length];
            test.setName(subject + " Testi " + (i + 1));
            test.setDescription(subject + " konusundaki bilgileri ölçen test");
            test.setDurationMinutes(60);
            Test savedTest = testRepository.save(test);
            allTests.add(savedTest);
            
            // Sorular oluştur
            for (int j = 0; j < QUESTIONS_PER_TEST; j++) {
                Question question = new Question();
                question.setTest(savedTest);
                String template = QUESTION_TEMPLATES[j % QUESTION_TEMPLATES.length];
                question.setContent(String.format(template, subject));
                question.setQuestionType(Question.QuestionType.MULTIPLE_CHOICE);
                question.setPoints(10);
                Question savedQuestion = questionRepository.save(question);
                
                // Seçenekler oluştur
                List<Option> options = new ArrayList<>();
                int correctOptionIndex = ThreadLocalRandom.current().nextInt(OPTIONS_PER_QUESTION);
                
                for (int k = 0; k < OPTIONS_PER_QUESTION; k++) {
                    Option option = new Option();
                    option.setQuestion(savedQuestion);
                    option.setContent("Seçenek " + (char)('A' + k));
                    option.setIsCorrect(k == correctOptionIndex);
                    options.add(option);
                }
                
                optionRepository.saveAll(options);
            }
            
            if ((i + 1) % 10 == 0) {
                logger.info("{} test ve bunların soruları eklendi", i + 1);
            }
        }
        
        return allTests;
    }
    
    @Transactional
    protected void createParticipationsAndAnswers(
            List<Student> students,
            List<Test> tests,
            TestParticipationRepository participationRepository,
            AnswerRepository answerRepository) {
        
        int totalParticipations = 0;
        int batchSize = 1000;
        List<TestParticipation> participationBatch = new ArrayList<>(batchSize);
        
        for (Student student : students) {
            // Random testler seç
            List<Test> randomTests = getRandomTests(tests, PARTICIPATIONS_PER_STUDENT);
            
            for (Test test : randomTests) {
                TestParticipation participation = new TestParticipation();
                participation.setStudent(student);
                participation.setTest(test);
                participation.setStartTime(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(30)));
                participation.setEndTime(participation.getStartTime().plusMinutes(ThreadLocalRandom.current().nextInt(10, 60)));
                participation.setStatus(TestParticipation.ParticipationStatus.COMPLETED);
                
                participationBatch.add(participation);
                totalParticipations++;
                
                // Batch insert için kontrol
                if (participationBatch.size() >= batchSize) {
                    saveParticipationsWithAnswers(participationBatch, answerRepository);
                    participationBatch.clear();
                    logger.info("{} katılım ve bunların cevapları eklendi", totalParticipations);
                }
            }
        }
        
        // Kalan katılımları kaydet
        if (!participationBatch.isEmpty()) {
            saveParticipationsWithAnswers(participationBatch, answerRepository);
            logger.info("Toplam {} katılım ve bunların cevapları eklendi", totalParticipations);
        }
    }
    
    @Transactional
    protected void saveParticipationsWithAnswers(
            List<TestParticipation> participations,
            AnswerRepository answerRepository) {
        
        for (TestParticipation participation : participations) {
            // Önce katılımı kaydet
            TestParticipation savedParticipation = participation;
            
            // Bu testteki tüm soruları al
            List<Question> questions = participation.getTest().getQuestions();
            
            // Her soru için cevap oluştur
            List<Answer> answers = new ArrayList<>();
            int totalScore = 0;
            
            for (Question question : questions) {
                Answer answer = new Answer();
                answer.setParticipation(savedParticipation);
                answer.setQuestion(question);
                
                // Rastgele bir seçenek seç
                List<Option> options = question.getOptions();
                if (!options.isEmpty()) {
                    Option selectedOption = options.get(ThreadLocalRandom.current().nextInt(options.size()));
                    answer.setSelectedOption(selectedOption);
                    answer.setIsCorrect(selectedOption.getIsCorrect());
                    
                    if (selectedOption.getIsCorrect()) {
                        answer.setPointsEarned(question.getPoints());
                        totalScore += question.getPoints();
                    } else {
                        answer.setPointsEarned(0);
                    }
                }
                
                answers.add(answer);
            }
            
            // Toplam puanı güncelle
            savedParticipation.setScore(totalScore);
            
            // Tüm cevapları batch halinde kaydet
            answerRepository.saveAll(answers);
        }
    }
    
    private List<Test> getRandomTests(List<Test> tests, int count) {
        if (count >= tests.size()) {
            return tests;
        }
        
        Set<Integer> selectedIndices = new HashSet<>();
        while (selectedIndices.size() < count) {
            selectedIndices.add(ThreadLocalRandom.current().nextInt(tests.size()));
        }
        
        return selectedIndices.stream()
                .map(tests::get)
                .collect(Collectors.toList());
    }
}