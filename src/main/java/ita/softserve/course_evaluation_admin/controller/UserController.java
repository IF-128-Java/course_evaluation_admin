package ita.softserve.course_evaluation_admin.controller;

import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam int page, @RequestParam int size, @RequestParam String filter, @RequestParam String order, @RequestParam String direction) {
        PageRequest pageRequest = PageRequest
                .of(page, size, direction.equals("ASC") ? Sort.by(order).ascending() : Sort.by(order).descending());
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAllUserDto(filter, pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserDtoById(id));
    }

    @PatchMapping("/add-roles")
    public ResponseEntity<UserDto> updateRole(@RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateRoles(dto.getId(), dto.getRoles()));
    }
}
