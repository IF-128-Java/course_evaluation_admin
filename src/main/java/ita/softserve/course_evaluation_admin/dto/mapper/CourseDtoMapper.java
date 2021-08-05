package ita.softserve.course_evaluation_admin.dto.mapper;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.entity.Course;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CourseDtoMapper {
    public static CourseDto toDto(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .description(course.getDescription())
                .endDate(course.getEndDate())
                .startDate(course.getStartDate())
                .teacherDto(UserDtoMapper.toDto(course.getTeacher()))
                .build();
    }

    public static Course fromDto(CourseDto dto) {
        return Course.builder()
                .id(dto.getId())
                .courseName(dto.getCourseName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .teacher(UserDtoMapper.fromDto(dto.getTeacherDto()))
                .build();
    }

    public static List<CourseDto> toDto(List<Course> courses) {
        return Objects.isNull(courses) ? Collections.emptyList() : courses.stream().map(CourseDtoMapper::toDto).collect(Collectors.toList());
    }

    public List<Course> fromDto(List<CourseDto> dtos) {
        return Objects.isNull(dtos) ? Collections.emptyList() : dtos.stream().map(CourseDtoMapper::fromDto).collect(Collectors.toList());
    }
}
