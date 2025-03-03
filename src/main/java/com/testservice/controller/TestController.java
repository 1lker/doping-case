package com.testservice.controller;

import com.testservice.dto.request.TestRequest;
import com.testservice.dto.response.TestResponse;
import com.testservice.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@Tag(name = "Test İşlemleri", description = "Test CRUD işlemleri")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    @Operation(summary = "Tüm testleri listeler")
    public ResponseEntity<List<TestResponse>> getAllTests() {
        return ResponseEntity.ok(testService.getAllTests());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre test getirir")
    public ResponseEntity<TestResponse> getTestById(@PathVariable Long id) {
        return ResponseEntity.ok(testService.getTestById(id));
    }

    @PostMapping
    @Operation(summary = "Yeni test oluşturur")
    public ResponseEntity<TestResponse> createTest(@Valid @RequestBody TestRequest request) {
        return new ResponseEntity<>(testService.createTest(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Test bilgilerini günceller")
    public ResponseEntity<TestResponse> updateTest(@PathVariable Long id, @Valid @RequestBody TestRequest request) {
        return ResponseEntity.ok(testService.updateTest(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Testi siler")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }
}