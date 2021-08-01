package ita.softserve.course_evaluation_admin.dto.group;

import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;

import java.util.stream.Collectors;

public class GroupStudentDtoMapper {
    public static GroupStudentDto toDto(Group group) {
        return new GroupStudentDto(group.getId(), group.getGroupName(), group.getStudents().stream().map(User::getId).collect(Collectors.toList()));
    }
}
