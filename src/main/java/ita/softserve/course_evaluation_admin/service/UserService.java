package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserDto> findAllUserDto();

    User findById(long id);

    UserDto updateRoles(long userId, Set<Role> roles);

    UserDto findUserDtoById(long id);
}
