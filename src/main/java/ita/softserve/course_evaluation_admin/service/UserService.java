package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {

    Page<UserDto> findAllUserDto(String search, Integer[] roles, Pageable pageable );

    User findById(long id);

    UserDto updateRoles(long userId, Set<Role> roles);

    UserDto findUserDtoById(long id);
}
