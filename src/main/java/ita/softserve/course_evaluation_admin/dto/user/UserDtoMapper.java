package ita.softserve.course_evaluation_admin.dto.user;

import ita.softserve.course_evaluation_admin.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDtoMapper {
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
        UserDtoMapper userDtoMapper = new UserDtoMapper();
        return Objects.isNull(users) ? Collections.emptyList() : users.stream().map(UserDtoMapper::toDto).collect(Collectors.toList());
    }

    public List<User> fromDto(List<UserDto> entities) {
        UserDtoMapper userDtoMapper = new UserDtoMapper();
        return Objects.isNull(entities) ? Collections.emptyList() : entities.stream().map(UserDtoMapper::fromDto).collect(Collectors.toList());
    }
}
