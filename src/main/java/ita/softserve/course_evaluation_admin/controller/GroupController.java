package ita.softserve.course_evaluation_admin.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation_admin.constants.HttpStatuses;
import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.GroupDtoMapper;
import ita.softserve.course_evaluation_admin.service.CourseService;
import ita.softserve.course_evaluation_admin.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/groups")
@Validated
public class GroupController {
    private final GroupService groupService;
    private final CourseService courseService;

    public GroupController(GroupService groupService, CourseService courseService) {
        this.groupService = groupService;
        this.courseService = courseService;
    }

    @ApiOperation(value = "Get all Groups list by sized pages and sorted in some direction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
    })
    @GetMapping
    public ResponseEntity<Page<GroupDto>> getAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findAllGroupDto(PageRequest.of(page, size)));
    }

    @ApiOperation(value = "Get Group by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = GroupDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findGroupDtoById(id));
    }

    @ApiOperation(value = "Get courses by group Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = GroupDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/{id}/courses")
    public ResponseEntity<Page<CourseDto>> getCoursesByGroupId(@PathVariable long id, @RequestParam String filter,
                                                               @RequestParam int page, @RequestParam int size,
                                                               @RequestParam String direction, @RequestParam String order,
                                                               @RequestParam List<String> status) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.
                findCourseDtoByGroupId(id, filter, PageRequest.of(page, size, direction.equals("ASC") ? Sort.by(order).ascending() : Sort.by(order).descending()), status));
    }

    @ApiOperation(value = "Get Available courses by group Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = GroupDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/{id}/available-courses")
    public ResponseEntity<Page<CourseDto>> getAvailableCoursesToAdd(@PathVariable long id, @RequestParam String filter, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.findAllByFilterAndExcludeGroup(id, filter, PageRequest.of(page, size)));
    }

    @ApiOperation(value = "Create a new Group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody @NotBlank(message = "Group name should be not empty") @Size(min = 3, max = 20, message = "Group name must be between 3 and 20") String groupName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupDtoMapper.toDto(groupService.create(groupName)));
    }

    @ApiOperation(value = "Update Group name by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<GroupDto> updateGroupName(@PathVariable long id, @RequestBody @NotBlank(message = "Group name should be not empty") @Size(min = 3, max = 20, message = "Group name must be between 3 and 20") String groupName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupDtoMapper.toDto(groupService.updateName(id, groupName)));
    }

    @ApiOperation(value = "Update Group by ID through the adding students in")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PatchMapping("/{id}/add-students")
    public ResponseEntity<GroupDto> addStudents(@PathVariable long id, @RequestBody List<UserDto> users) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GroupDtoMapper.toDto(groupService.addStudents(id, users)));
    }

    @ApiOperation(value = "Update Group by ID through the deleting students from")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PatchMapping("/{id}/remove-students")
    public ResponseEntity<GroupDto> removeStudents(@PathVariable long id, @RequestBody List<UserDto> dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GroupDtoMapper.toDto(groupService.removeStudents(id, dto)));
    }

    @ApiOperation(value = "Update Group by ID through the adding courses in ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PatchMapping("/{id}/add-course")
    public ResponseEntity<GroupDto> addCourse(@PathVariable long id, @RequestBody CourseDto course) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.addCourse(id, course));
    }

    @ApiOperation(value = "Update Group by ID through the deleting courses from")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PatchMapping("/{id}/remove-course")
    public ResponseEntity<GroupDto> removeCourse(@PathVariable long id, @RequestBody CourseDto course) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.removeCourse(id, course));
    }

    @ApiOperation(value = "Delete Group by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroup(@PathVariable long id) {
        groupService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
