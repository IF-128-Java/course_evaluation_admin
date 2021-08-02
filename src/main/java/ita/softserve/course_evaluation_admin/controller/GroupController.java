package ita.softserve.course_evaluation_admin.controller;


import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.GroupDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.service.GroupService;
import ita.softserve.course_evaluation_admin.service.UserService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/admin/groups")
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(GroupDtoMapper.toDto(groupService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(GroupDtoMapper.toDto(groupService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody String groupName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupDtoMapper.toDto(groupService.create(groupName)));

    }

    @PatchMapping("/add-students")
    public ResponseEntity<GroupDto> addStudents(@RequestBody GroupDto dto) {
        final Group groupFound = groupService.findById(dto.getId());
        final List<User> usersFound = dto.getStudents()
                .stream()
                .map(UserDto::getId)
                .map(userService::findById)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
                .body(GroupDtoMapper.toDto(groupService.addStudents(groupFound, usersFound)));
    }

    @PatchMapping("/remove-students")
    public ResponseEntity<GroupDto> removeStudents(@RequestBody GroupDto dto) {
        final Group groupFound = groupService.findById(dto.getId());
        final List<User> usersFound = dto.getStudents()
                .stream()
                .map(UserDto::getId)
                .map(userService::findById)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
                .body(GroupDtoMapper.toDto(groupService.removeStudents(groupFound, usersFound)));
    }
}
