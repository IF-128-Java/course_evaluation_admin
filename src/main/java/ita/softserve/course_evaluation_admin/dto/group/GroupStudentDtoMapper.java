package ita.softserve.course_evaluation_admin.dto.group;

import ita.softserve.course_evaluation_admin.dto.user.UserDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Group;

public class GroupStudentDtoMapper {
    public static GroupStudentDto toDto(Group group) {
        return new GroupStudentDto(group.getId(), group.getGroupName(), UserDtoMapper.toDto(group.getStudents()));
    }
}
