package ita.softserve.course_evaluation_admin.dto.mapper;

import ita.softserve.course_evaluation_admin.dto.StudentDto;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StudentDtoMapper {
    public static StudentDto toDto(User user) {
        StudentDto studentDto = StudentDto.builder()
                .userDto(UserDtoMapper.toDto(user))
                .build();
        if (user.getGroup() != null) {
            studentDto.setGroupId(user.getGroup().getId());
            studentDto.setGroupName(user.getGroup().getGroupName());
        }
        return studentDto;
    }

    public static User fromDto(StudentDto dto) {
        User user = UserDtoMapper.fromDto(dto.getUserDto());
        user.setGroup(Group.builder()
                .id(dto.getGroupId())
                .groupName(dto.getGroupName())
                .build());
        return user;
    }

    public static List<StudentDto> toDto(List<User> users) {
        return Objects.isNull(users) ? Collections.emptyList() : users.stream().map(StudentDtoMapper::toDto).collect(Collectors.toList());
    }

    public List<User> fromDto(List<StudentDto> dtos) {
        return Objects.isNull(dtos) ? Collections.emptyList() : dtos.stream().map(StudentDtoMapper::fromDto).collect(Collectors.toList());
    }
}
