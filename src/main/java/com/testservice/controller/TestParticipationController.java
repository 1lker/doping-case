package com.testservice.controller;

import com.testservice.dto.request.AnswerRequest;
import com.testservice.dto.request.TestParticipationRequest;
import com.testservice.dto.response.TestParticipationResponse;
import com.testservice.service.TestParticipationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
@Tag(name = "Test Katılım İşlemleri", description = "Test katılım ve cevaplama işlemleri")
public class TestParticipationController {

    private final TestParticipationService participationService;

    @Autowired
    public TestParticipationController(TestParticipationService participationService) {
        this.participationService = participationService;
    }

    @GetMapping
    @Operation(summary = "Tüm test katılımlarını listeler", description = "Sayfalama özellikleri ile tüm test katılımlarını listeler. Varsayılan sayfa boyutu 10'dur.")
    public ResponseEntity<Page<TestParticipationResponse>> getAllParticipations(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(participationService.getAllParticipationsPaged(pageable));
    }

    @GetMapping("/all")
    @Operation(summary = "Tüm test katılımlarını sayfalanmadan listeler")
    public ResponseEntity<List<TestParticipationResponse>> getAllParticipationsWithoutPagination() {
        return ResponseEntity.ok(participationService.getAllParticipations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre test katılımı getirir")
    public ResponseEntity<TestParticipationResponse> getParticipationById(@PathVariable Long id) {
        return ResponseEntity.ok(participationService.getParticipationById(id));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Öğrenci ID'sine göre test katılımlarını listeler")
    public ResponseEntity<List<TestParticipationResponse>> getParticipationsByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(participationService.getParticipationsByStudentId(studentId));
    }

    @GetMapping("/test/{testId}")
    @Operation(summary = "Test ID'sine göre test katılımlarını listeler")
    public ResponseEntity<List<TestParticipationResponse>> getParticipationsByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(participationService.getParticipationsByTestId(testId));
    }

    @PostMapping("/start")
    @Operation(summary = "Test katılımı başlatır")
    public ResponseEntity<TestParticipationResponse> startTest(@Valid @RequestBody TestParticipationRequest request) {
        return new ResponseEntity<>(participationService.startTest(request), HttpStatus.CREATED);
    }

    @PostMapping("/answer")
    @Operation(summary = "Test sorusuna cevap gönderir")
    public ResponseEntity<TestParticipationResponse> submitAnswer(@Valid @RequestBody AnswerRequest request) {
        return ResponseEntity.ok(participationService.submitAnswer(request));
    }

    @PostMapping("/{participationId}/finish")
    @Operation(summary = "Test katılımını tamamlar")
    public ResponseEntity<TestParticipationResponse> finishTest(@PathVariable Long participationId) {
        return ResponseEntity.ok(participationService.finishTest(participationId));
    }
}