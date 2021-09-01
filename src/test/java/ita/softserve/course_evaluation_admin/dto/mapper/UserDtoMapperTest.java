package ita.softserve.course_evaluation_admin.dto.mapper;

import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoMapperTest {
    private User mike;
    private User joe;
    private UserDto mikeDto;
    private UserDto joeDto;

    @BeforeEach
    void setUp() {
        Set<Role> studentRoles = new HashSet<>();
        Set<Role> allRoles = new HashSet<>();
        studentRoles.add(Role.ROLE_STUDENT);
        allRoles.add(Role.ROLE_STUDENT);
        allRoles.add(Role.ROLE_ADMIN);
        allRoles.add(Role.ROLE_TEACHER);
        mike = User.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Bernardo")
                .roles(studentRoles)
                .build();
        joe = User.builder()
                .id(2L)
                .firstName("Joe")
                .lastName("Green")
                .roles(allRoles)
                .build();
        mikeDto = UserDto.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Bernardo")
                .roles(studentRoles)
                .build();
        joeDto = UserDto.builder()
                .id(2L)
                .firstName("Joe")
                .lastName("Green")
                .roles(allRoles)
                .build();
    }

    @Test
    void toDtoMike() {
        UserDto actual = UserDtoMapper.toDto(mike);
        UserDto expected = mikeDto;
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void toDtoJoe() {
        UserDto actual = UserDtoMapper.toDto(joe);
        UserDto expected = joeDto;
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void fromDtoMike() {
        User actual = UserDtoMapper.fromDto(mikeDto);
        User expected = mike;
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void fromDtoJoe() {
        User actual = UserDtoMapper.fromDto(joeDto);
        User expected = joe;
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void fromDtoList() {
        List<User> actual = UserDtoMapper.fromDto(List.of(joeDto, mikeDto));
        List<User> expected = List.of(joe, mike);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void fromDtoEmptyList() {
        List<User> actual = UserDtoMapper.fromDto(Collections.emptyList());
        assertTrue(actual.isEmpty());
    }

    @Test
    void toDtoList() {
        List<UserDto> actual = UserDtoMapper.toDto(List.of(joe, mike));
        List<UserDto> expected = List.of(joeDto, mikeDto);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void toDtoEmptyList() {
        List<UserDto> actual = UserDtoMapper.toDto(Collections.emptyList());
        assertTrue(actual.isEmpty());
    }
}