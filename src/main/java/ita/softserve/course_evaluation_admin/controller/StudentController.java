package ita.softserve.course_evaluation_admin.controller;

import ita.softserve.course_evaluation_admin.dto.StudentDto;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Page<StudentDto>> getStudentCandidates(@RequestParam String filter,@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.findStudentsNotIncludeGroup(Role.ROLE_STUDENT.ordinal(),filter,PageRequest.of(page,size)));
    }
    @GetMapping
    public ResponseEntity<Page<StudentDto>> getAllStudentsFromGroups(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.findAllStudents(PageRequest.of(page,size)));
    }
}
