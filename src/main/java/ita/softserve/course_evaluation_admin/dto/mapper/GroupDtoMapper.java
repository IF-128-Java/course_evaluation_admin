package ita.softserve.course_evaluation_admin.dto.mapper;

import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.entity.Group;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GroupDtoMapper {
    public static GroupDto toDto(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .students(UserDtoMapper.toDto(group.getStudents()))
                .build();
    }

    public static Group fromDto(GroupDto dto) {
        return Group.builder()
                .id(dto.getId())
                .groupName(dto.getGroupName())
                .students(UserDtoMapper.fromDto(dto.getStudents()))
                .build();
    }

    public static List<GroupDto> toDto(List<Group> groups) {
        return Objects.isNull(groups) ? Collections.emptyList() : groups.stream().map(GroupDtoMapper::toDto).collect(Collectors.toList());
    }

    public List<Group> fromDto(List<GroupDto> dtos) {
        return Objects.isNull(dtos) ? Collections.emptyList() : dtos.stream().map(GroupDtoMapper::fromDto).collect(Collectors.toList());
    }
}
