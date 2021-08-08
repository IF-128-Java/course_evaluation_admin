package ita.softserve.course_evaluation_admin.controller;


import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.GroupDtoMapper;
import ita.softserve.course_evaluation_admin.service.CourseService;
import ita.softserve.course_evaluation_admin.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/groups")
public class GroupController {
    private final GroupService groupService;
    private final CourseService courseService;

    public GroupController(GroupService groupService, CourseService courseService) {
        this.groupService = groupService;
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<Page<GroupDto>> getAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findAllGroupDto(PageRequest.of(page,size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findGroupDtoById(id));
    }
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseDto>> getCoursesByGroupId(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findCourseDtoByGroupId(id));
    }
    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody String groupName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupDtoMapper.toDto(groupService.create(groupName)));

    }

    @PatchMapping("/{id}/add-students")
    public ResponseEntity<GroupDto> addStudents(@PathVariable long id, @RequestBody List<UserDto> users) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GroupDtoMapper.toDto(groupService.addStudents(id, users)));
    }

    @PatchMapping("/{id}/remove-students")
    public ResponseEntity<GroupDto> removeStudents(@PathVariable long id, @RequestBody List<UserDto> dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GroupDtoMapper.toDto(groupService.removeStudents(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroup(@PathVariable long id) {
        groupService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
