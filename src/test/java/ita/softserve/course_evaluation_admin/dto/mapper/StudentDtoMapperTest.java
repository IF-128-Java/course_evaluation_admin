package ita.softserve.course_evaluation_admin.dto.mapper;

import ita.softserve.course_evaluation_admin.dto.StudentDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentDtoMapperTest {

    private User mike;
    private User joe;
    private StudentDto mikeStudentDto;
    private StudentDto joeStudentDto;

    @BeforeEach
    void setUp() {
        Set<Role> studentRoles = new HashSet<>();
        Set<Role> allRoles = new HashSet<>();
        studentRoles.add(Role.ROLE_STUDENT);
        allRoles.add(Role.ROLE_STUDENT);
        allRoles.add(Role.ROLE_ADMIN);
        allRoles.add(Role.ROLE_TEACHER);
        Group if128 = Group.builder()
                .id(1L)
                .groupName("if128")
                .build();
        Group lv120 = Group.builder()
                .id(2L)
                .groupName("lv120")
                .build();
        mike = User.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Bernardo")
                .roles(studentRoles)
                .group(if128)
                .build();
        joe = User.builder()
                .id(2L)
                .firstName("Joe")
                .lastName("Green")
                .roles(allRoles)
                .group(lv120)
                .build();
        UserDto mikeDto = UserDto.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Bernardo")
                .roles(studentRoles)
                .build();
        UserDto joeDto = UserDto.builder()
                .id(2L)
                .firstName("Joe")
                .lastName("Green")
                .roles(allRoles)
                .build();
        joeStudentDto = StudentDto.builder()
                .userDto(joeDto)
                .groupId(lv120.getId())
                .groupName(lv120.getGroupName())
                .build();
        mikeStudentDto = StudentDto.builder()
                .userDto(mikeDto)
                .groupId(if128.getId())
                .groupName(if128.getGroupName())
                .build();
    }

    @Test
    void toDtoMike() {
        StudentDto actual = StudentDtoMapper.toDto(mike);
        StudentDto expected = mikeStudentDto;
        assertEquals(expected, actual);
    }

    @Test
    void toDtoJoe() {
        StudentDto actual = StudentDtoMapper.toDto(joe);
        StudentDto expected = joeStudentDto;
        assertEquals(expected, actual);
    }

    @Test
    void fromDtoJoe() {
        User actual = StudentDtoMapper.fromDto(joeStudentDto);
        User expected = joe;
        assertEquals(expected, actual);
    }

    @Test
    void fromDtoMike() {
        User actual = StudentDtoMapper.fromDto(mikeStudentDto);
        User expected = mike;
        assertEquals(expected, actual);
    }

    @Test
    void testToDto() {
        List<StudentDto> actual = StudentDtoMapper.toDto(List.of(joe, mike));
        List<StudentDto> expected = List.of(joeStudentDto, mikeStudentDto);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void testFromDto() {
        List<User> actual = StudentDtoMapper.fromDto(List.of(joeStudentDto, mikeStudentDto));
        List<User> expected = List.of(joe, mike);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void fromDtoEmptyList() {
        List<User> actual = StudentDtoMapper.fromDto(Collections.emptyList());
        assertTrue(actual.isEmpty());
    }

    @Test
    void toDtoEmptyList() {
        List<StudentDto> actual = StudentDtoMapper.toDto(Collections.emptyList());
        assertTrue(actual.isEmpty());
    }
}