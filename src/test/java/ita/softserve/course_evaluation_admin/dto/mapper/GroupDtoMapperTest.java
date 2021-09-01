package ita.softserve.course_evaluation_admin.dto.mapper;

import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GroupDtoMapperTest {

    private Group if128;
    private GroupDto if128Dto;
    private Group lv120;
    private GroupDto lv120Dto;

    @BeforeEach
    void setUp() {
        User mike = User.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Bernardo")
                .group(if128)
                .build();
        User joe = User.builder()
                .id(2L)
                .firstName("Joe")
                .lastName("Green")
                .group(lv120)
                .build();
        UserDto mikeDto = UserDto.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Bernardo")
                .build();
        UserDto joeDto = UserDto.builder()
                .id(2L)
                .firstName("Joe")
                .lastName("Green")
                .build();
        List<User> students = List.of(mike, joe);
        if128 = Group.builder()
                .id(1L)
                .groupName("if128")
                .students(students)
                .build();
        lv120 = Group.builder()
                .id(2L)
                .groupName("lv120")
                .students(Collections.emptyList())
                .build();
        if128Dto = GroupDto.builder()
                .id(1L)
                .groupName("if128")
                .students(List.of(mikeDto, joeDto))
                .build();
        lv120Dto = GroupDto.builder()
                .id(2L)
                .groupName("lv120")
                .students(Collections.emptyList())
                .build();
    }

    @Test
    void toDtoIf128() {
        GroupDto actual = GroupDtoMapper.toDto(if128);
        GroupDto expected = if128Dto;
        assertEquals(expected, actual);
    }

    @Test
    void toDtoLv120() {
        GroupDto actual = GroupDtoMapper.toDto(lv120);
        GroupDto expected = lv120Dto;
        assertEquals(expected, actual);
    }

    @Test
    void fromDtoIf128() {
        Group actual = GroupDtoMapper.fromDto(if128Dto);
        Group expected = if128;
        assertEquals(expected, actual);
    }

    @Test
    void fromDtoLv120() {
        Group actual = GroupDtoMapper.fromDto(lv120Dto);
        Group expected = lv120;
        assertEquals(expected, actual);
    }

    @Test
    void testToList() {
        List<GroupDto> actual = GroupDtoMapper.toDto(List.of(lv120, if128));
        List<GroupDto> expected = List.of(if128Dto, lv120Dto);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void testFromList() {
        List<Group> actual = GroupDtoMapper.fromDto(List.of(if128Dto, lv120Dto));
        List<Group> expected = List.of(if128, lv120);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void fromDtoEmptyList() {
        List<Group> actual = GroupDtoMapper.fromDto(Collections.emptyList());
        assertTrue(actual.isEmpty());
    }

    @Test
    void toDtoEmptyList() {
        List<GroupDto> actual = GroupDtoMapper.toDto(Collections.emptyList());
        assertTrue(actual.isEmpty());
    }
}