package ita.softserve.course_evaluation_admin.dto.group;

import ita.softserve.course_evaluation_admin.entity.Group;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GroupDtoMapper {
    public static GroupDto toDto(Group group) {
        return new GroupDto(group.getId(), group.getGroupName());
    }

    public static List<GroupDto> toDto(List<Group> groups) {
        return Objects.isNull(groups) ? Collections.emptyList() : groups.stream().map(GroupDtoMapper::toDto).collect(Collectors.toList());
    }

    public static Group fromDto(GroupDto dto) {
        Group group = new Group();
        group.setId(dto.getId());
        group.setGroupName(dto.getGroupName());
        return group;

    }
}
