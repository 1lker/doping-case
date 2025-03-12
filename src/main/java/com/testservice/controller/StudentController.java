package com.testservice.controller;

import com.testservice.dto.request.StudentRequest;
import com.testservice.dto.response.StudentResponse;
import com.testservice.service.StudentService;
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
@RequestMapping("/api/students")
@Tag(name = "Öğrenci İşlemleri", description = "Öğrenci CRUD işlemleri")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @Operation(summary = "Tüm öğrencileri listeler", description = "Sayfalama özellikleri ile tüm öğrencileri listeler. Varsayılan sayfa boyutu 10'dur.")
    public ResponseEntity<Page<StudentResponse>> getAllStudents(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudentsPaged(pageable));
    }

    @GetMapping("/all")
    @Operation(summary = "Tüm öğrencileri sayfalanmadan listeler")
    public ResponseEntity<List<StudentResponse>> getAllStudentsWithoutPagination() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre öğrenci getirir")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/number/{studentNumber}")
    @Operation(summary = "Öğrenci numarasına göre öğrenci getirir")
    public ResponseEntity<StudentResponse> getStudentByStudentNumber(@PathVariable String studentNumber) {
        return ResponseEntity.ok(studentService.getStudentByStudentNumber(studentNumber));
    }

    @PostMapping
    @Operation(summary = "Yeni öğrenci oluşturur")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        return new ResponseEntity<>(studentService.createStudent(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Öğrenci bilgilerini günceller")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Öğrenciyi siler")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}