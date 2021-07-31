package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.user.UserDto;
import ita.softserve.course_evaluation_admin.dto.user.UserRolesDto;
import ita.softserve.course_evaluation_admin.entity.User;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    User findById(long id);

    User updateRoles(UserRolesDto dto);


}
