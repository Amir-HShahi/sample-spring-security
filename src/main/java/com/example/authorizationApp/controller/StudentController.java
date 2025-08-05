/**
 * Controller class for managing student-related operations in the authorization application.
 * Provides REST endpoints for student CRUD operations and CSRF token retrieval.
 */
package com.example.authorizationApp.controller;

import com.example.authorizationApp.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for handling student-related HTTP requests.
 */
@RestController
public class StudentController {
    /**
     * In-memory list to store student records.
     */
    List<Student> students = new ArrayList<>();

    /**
     * Retrieves all students from the system.
     *
     * @return List of all students
     */
    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Retrieves the CSRF token for security purposes.
     *
     * @param request The HTTP servlet request containing the CSRF token
     * @return The CSRF token object
     */
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    /**
     * Creates a new student in the system.
     *
     * @param student The student object to be created
     * @return The created student object
     */
    @PostMapping("/students")
    public Student createStudent(@RequestBody Student student) {
        students.add(student);
        return student;
    }
 }
