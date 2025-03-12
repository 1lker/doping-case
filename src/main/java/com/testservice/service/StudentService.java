package com.testservice.service;

import com.testservice.dto.request.StudentRequest;
import com.testservice.dto.response.StudentResponse;
import com.testservice.exception.BadRequestException;
import com.testservice.exception.ResourceNotFoundException;
import com.testservice.model.Student;
import com.testservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Cacheable("students")
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "studentsPage", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<StudentResponse> getAllStudentsPaged(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Cacheable(value = "student", key = "#id")
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı. ID: " + id));
        return mapToResponse(student);
    }

    @Cacheable(value = "studentByNumber", key = "#studentNumber")
    public StudentResponse getStudentByStudentNumber(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı. Öğrenci No: " + studentNumber));
        return mapToResponse(student);
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByStudentNumber(request.getStudentNumber())) {
            throw new BadRequestException("Bu öğrenci numarası zaten kullanılıyor: " + request.getStudentNumber());
        }

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setStudentNumber(request.getStudentNumber());

        Student savedStudent = studentRepository.save(student);
        return mapToResponse(savedStudent);
    }

    @Transactional
    @CacheEvict(value = {"student", "studentByNumber", "students"}, key = "#id", allEntries = true)
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı. ID: " + id));

        // Eğer öğrenci numarası değiştiyse ve yeni numara başka bir öğrenci tarafından kullanılıyorsa hata fırlat
        if (!student.getStudentNumber().equals(request.getStudentNumber()) &&
                studentRepository.existsByStudentNumber(request.getStudentNumber())) {
            throw new BadRequestException("Bu öğrenci numarası zaten kullanılıyor: " + request.getStudentNumber());
        }

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setStudentNumber(request.getStudentNumber());

        Student updatedStudent = studentRepository.save(student);
        return mapToResponse(updatedStudent);
    }

    @Transactional
    @CacheEvict(value = {"student", "studentByNumber", "students"}, key = "#id", allEntries = true)
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Öğrenci bulunamadı. ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .studentNumber(student.getStudentNumber())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}