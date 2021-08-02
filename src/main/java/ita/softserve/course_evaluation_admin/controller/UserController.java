package ita.softserve.course_evaluation_admin.controller;

import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.UserDtoMapper;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(UserDtoMapper.toDto(userService.findAll()));
    }

    @PatchMapping("/add-roles")
    public ResponseEntity<UserDto> updateRole(@RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserDtoMapper.toDto(userService.updateRoles(userService.findById(dto.getId()), dto.getRoles())));
    }
}
