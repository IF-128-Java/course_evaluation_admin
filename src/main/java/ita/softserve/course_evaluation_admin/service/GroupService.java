package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupService {
    Page<GroupDto> findAllGroupDto(Pageable pageable);

    Group findById(long id);

    GroupDto findGroupDtoById(long id);

    Group addStudents(long groupId, List<UserDto> users);

    Group removeStudents(long groupId, List<UserDto> users);

    void deleteById(long id);

    Group create(String userName);
}
