package ita.softserve.course_evaluation_admin.dto.group;

import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;

import java.util.stream.Collectors;

public class GroupStudentRequestDtoMapper {
    public static GroupStudentRequestDto toDto(Group group) {
        return new GroupStudentRequestDto(group.getId(), group.getGroupName(), group.getStudents().stream().map(User::getId).collect(Collectors.toList()));
    }
}
