package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.group.GroupDto;
import ita.softserve.course_evaluation_admin.dto.group.GroupListDto;
import ita.softserve.course_evaluation_admin.dto.group.GroupStudentDto;
import ita.softserve.course_evaluation_admin.entity.Group;

import java.util.List;

public interface GroupService {
    List<GroupListDto> findAll();

    Group findById(long id);

    GroupStudentDto addStudents(GroupStudentDto dto);

    GroupStudentDto removeStudents(GroupStudentDto dto);

    GroupDto getGroupProfile(long id);
}
