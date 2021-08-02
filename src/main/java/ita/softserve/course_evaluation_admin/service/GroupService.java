package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.group.GroupStudentDto;
import ita.softserve.course_evaluation_admin.dto.group.GroupDto;
import ita.softserve.course_evaluation_admin.dto.group.GroupStudentRequestDto;
import ita.softserve.course_evaluation_admin.entity.Group;

import java.util.List;

public interface GroupService {
    List<GroupDto> findAll();

    Group findById(long id);

    GroupStudentRequestDto addStudents(GroupStudentRequestDto dto);

    GroupStudentRequestDto removeStudents(GroupStudentRequestDto dto);

    GroupStudentDto getGroupProfile(long id);

    Group create(GroupDto dto);
}
