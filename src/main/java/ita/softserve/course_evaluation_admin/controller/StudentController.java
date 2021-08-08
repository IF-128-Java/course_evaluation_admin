package ita.softserve.course_evaluation_admin.controller;

import ita.softserve.course_evaluation_admin.dto.StudentDto;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<StudentDto>> getStudentCandidates() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.findStudentsNotIncludeGroup(Role.ROLE_STUDENT.ordinal()));
    }
    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudentsFromGroups() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.findAllStudents(Role.ROLE_STUDENT.ordinal()));
    }
}
