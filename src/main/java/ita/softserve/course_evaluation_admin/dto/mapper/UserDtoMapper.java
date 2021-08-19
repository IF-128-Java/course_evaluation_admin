package ita.softserve.course_evaluation_admin.dto.mapper;

import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDtoMapper {
    private UserDtoMapper() {
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .build();
    }

    public static User fromDto(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .roles(dto.getRoles())
                .build();
    }

    public static List<UserDto> toDto(List<User> users) {
        return Objects.isNull(users) ? Collections.emptyList() : users.stream().map(UserDtoMapper::toDto).collect(Collectors.toList());
    }

    public static List<User> fromDto(List<UserDto> entities) {
        return Objects.isNull(entities) ? Collections.emptyList() : entities.stream().map(UserDtoMapper::fromDto).collect(Collectors.toList());
    }
}
