package ita.softserve.course_evaluation_admin.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation_admin.constants.HttpStatuses;
import ita.softserve.course_evaluation_admin.dto.CourseDto;
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

    @ApiOperation(value = "Get all Users list by sized pages and sorted in some direction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam int page, @RequestParam int size,
                                                @RequestParam String search, @RequestParam String order
            , @RequestParam String direction, @RequestParam Integer[] roles) {
        PageRequest pageRequest = PageRequest
                .of(page, size, direction.equals("ASC") ? Sort.by(order).ascending() : Sort.by(order).descending());
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAllUserDto(search, roles, pageRequest));
    }

    @ApiOperation(value = "Get User by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserDtoById(id));
    }

    @ApiOperation(value = "Update User role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = CourseDto.class),
            @ApiResponse(code = 303, message = HttpStatuses.SEE_OTHER),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PatchMapping("/add-roles")
    public ResponseEntity<UserDto> updateRole(@RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateRoles(dto.getId(), dto.getRoles()));
    }
}
