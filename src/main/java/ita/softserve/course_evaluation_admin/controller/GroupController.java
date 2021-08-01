package ita.softserve.course_evaluation_admin.controller;


import ita.softserve.course_evaluation_admin.dto.group.GroupStudentDto;
import ita.softserve.course_evaluation_admin.dto.group.GroupDto;
import ita.softserve.course_evaluation_admin.dto.group.GroupStudentRequestDto;
import ita.softserve.course_evaluation_admin.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupStudentDto> getById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupProfile(id));
    }

    @PostMapping
    public ResponseEntity<Object> createGroup(@RequestBody GroupDto dto) {
        groupService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PatchMapping("/add-students")
    public ResponseEntity<GroupStudentRequestDto> addStudents(@RequestBody GroupStudentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.addStudents(dto));
    }

    @PatchMapping("/remove-students")
    public ResponseEntity<GroupStudentRequestDto> removeStudents(@RequestBody GroupStudentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.removeStudents(dto));
    }
}
