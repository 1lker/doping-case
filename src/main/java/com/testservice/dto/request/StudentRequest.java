package com.testservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StudentRequest {
    @NotBlank(message = "Ad alanı boş olamaz")
    @Size(max = 50, message = "Ad en fazla 50 karakter olabilir")
    private String firstName;

    @NotBlank(message = "Soyad alanı boş olamaz")
    @Size(max = 50, message = "Soyad en fazla 50 karakter olabilir")
    private String lastName;

    @NotBlank(message = "Öğrenci numarası boş olamaz")
    @Size(max = 20, message = "Öğrenci numarası en fazla 20 karakter olabilir")
    private String studentNumber;

    // Constructors
    public StudentRequest() {
    }

    public StudentRequest(String firstName, String lastName, String studentNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentNumber = studentNumber;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}