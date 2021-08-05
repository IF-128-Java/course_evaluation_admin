package ita.softserve.course_evaluation_admin.controller;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) { this.courseService = courseService; }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.findById(id));
    }
    @PostMapping
    public ResponseEntity<CourseDto> createGroup(@RequestBody CourseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(courseService.create(dto));
    }
}
