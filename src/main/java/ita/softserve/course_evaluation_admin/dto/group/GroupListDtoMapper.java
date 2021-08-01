package ita.softserve.course_evaluation_admin.dto.group;

import ita.softserve.course_evaluation_admin.entity.Group;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GroupListDtoMapper {
    public static GroupListDto toDto(Group group) {
        return new GroupListDto(group.getId(), group.getGroupName());
    }

    public static List<GroupListDto> toDto(List<Group> groups) {
        return Objects.isNull(groups) ? Collections.emptyList() : groups.stream().map(GroupListDtoMapper::toDto).collect(Collectors.toList());
    }
}
