package ita.softserve.course_evaluation_admin.dto.mapper;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseDtoMapperTest {

    private Course java;
    private Course python;
    private CourseDto javaDto;
    private CourseDto pythonDto;

    @BeforeEach
    void setUp() {

        User mike = User.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Bernardo")
                .roles(Set.of(Role.ROLE_TEACHER))
                .build();
        User leon = User.builder()
                .id(2L)
                .firstName("Leon")
                .lastName("Green")
                .roles(Set.of(Role.ROLE_TEACHER))
                .build();

        UserDto mikeDto = UserDto.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Bernardo")
                .roles(Set.of(Role.ROLE_TEACHER))
                .build();

        UserDto leonDro = UserDto.builder()
                .id(2L)
                .firstName("Leon")
                .lastName("Green")
                .roles(Set.of(Role.ROLE_TEACHER))
                .build();

        java = Course.builder()
                .id(1L)
                .courseName("Java")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(20))
                .description("Description")
                .teacher(mike)
                .build();
        python = Course.builder()
                .id(2L)
                .courseName("Python")
                .startDate(LocalDateTime.now().plusDays(5))
                .endDate(LocalDateTime.now().plusDays(25))
                .description("Description")
                .teacher(leon)
                .build();
        javaDto = CourseDto.builder()
                .id(1L)
                .courseName("Java")
                .startDate(java.getStartDate())
                .endDate(java.getEndDate())
                .description("Description")
                .teacherDto(mikeDto)
                .build();
        pythonDto = CourseDto.builder()
                .id(2L)
                .courseName("Python")
                .startDate(python.getStartDate())
                .endDate(python.getEndDate())
                .description("Description")
                .teacherDto(leonDro)
                .build();
    }

    @Test
    void toDtoJava() {
        CourseDto actual = CourseDtoMapper.toDto(java);
        CourseDto expected = javaDto;
        assertEquals(expected, actual);
    }

    @Test
    void toDtoPython() {
        CourseDto actual = CourseDtoMapper.toDto(python);
        CourseDto expected = pythonDto;
        assertEquals(expected, actual);
    }

    @Test
    void fromDtoJava() {
        Course actual = CourseDtoMapper.fromDto(javaDto);
        Course expected = java;
        assertEquals(expected, actual);
    }

    @Test
    void fromDtoPython() {
        Course actual = CourseDtoMapper.fromDto(pythonDto);
        Course expected = python;
        assertEquals(expected, actual);
    }

    @Test
    void toDtoList() {
        List<CourseDto> actual = CourseDtoMapper.toDto(List.of(java, python));
        List<CourseDto> expected = List.of(javaDto, pythonDto);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void fromDtoList() {
        List<Course> actual = CourseDtoMapper.fromDto(List.of(javaDto, pythonDto));
        List<Course> expected = List.of(java, python);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void fromDtoEmptyList() {
        List<Course> actual = CourseDtoMapper.fromDto(Collections.emptyList());
        assertTrue(actual.isEmpty());
    }

    @Test
    void toDtoEmptyList() {
        List<CourseDto> actual = CourseDtoMapper.toDto(Collections.emptyList());
        assertTrue(actual.isEmpty());
    }
    @Test
    void fromDtoListIsNull() {
        List<Course> actual = CourseDtoMapper.fromDto((List<CourseDto>) null);
        assertTrue(actual.isEmpty());
    }

    @Test
    void toDtoListIsNul() {
        List<CourseDto> actual = CourseDtoMapper.toDto((List<Course>) null);
        assertTrue(actual.isEmpty());
    }
}