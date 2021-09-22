package ita.softserve.course_evaluation_admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation_admin.constants.HttpStatuses;
import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.mapper.CourseDtoMapper;
import ita.softserve.course_evaluation_admin.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Course service REST API")
@RestController
@RequestMapping("api/v1/admin/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @ApiOperation(value = "Get All courses List by sized pages and sorted in some direction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping
    public ResponseEntity<Page<CourseDto>> getAll(@RequestParam int page, @RequestParam int size,
                                                  @RequestParam String orderBy, @RequestParam String direction) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.findAllCourseDto(
                        PageRequest.of(page, size,
                        direction.equals("ASC") ?
                        Sort.by(orderBy).ascending() :
                        Sort.by(orderBy).descending())));
    }

    @ApiOperation(value = "Get course by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.findCourseDtoById(id));
    }

    @ApiOperation(value = "Create a new Course")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(CourseDtoMapper.toDto(courseService.create(dto)));
    }

    @ApiOperation(value = "Update Course with Patch")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PatchMapping("/edit/{id}")
    public ResponseEntity<CourseDto> updateCourse(@Valid @PathVariable long id, @RequestBody CourseDto courseDto) {
        courseDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(courseService.editCourse(courseDto));
    }

    @ApiOperation(value = "Get course by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("name/{courseName}")
    public ResponseEntity<List<CourseDto>> getCourseByName(@PathVariable String courseName) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getByName(courseName));
    }

    @ApiOperation(value = "Delete Course by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity.BodyBuilder deleteCourse(@PathVariable long id) {
        courseService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK);
    }
}
