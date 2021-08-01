package ita.softserve.course_evaluation_admin.dto.group;

import ita.softserve.course_evaluation_admin.dto.user.UserDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Group;

public class GroupDtoMapper {
    public static GroupDto toDto(Group group) {
        return new GroupDto(group.getId(), group.getGroupName(), UserDtoMapper.toDto(group.getStudents()));
    }
}
