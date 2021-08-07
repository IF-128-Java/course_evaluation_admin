package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.entity.Group;

import java.util.List;

public interface GroupService {
    List<GroupDto> findAllUserDto();

    Group findById(long id);

    GroupDto findGroupDtoById(long id);

    Group addStudents(long group_id, List<UserDto> users);

    Group removeStudents(long group_id, List<UserDto> users);

    void deleteById(long id);

    Group create(String userName);
}
