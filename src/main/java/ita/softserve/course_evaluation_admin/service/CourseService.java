package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.entity.Course;

import java.util.List;

public interface CourseService {
    List<CourseDto> findAllCourseDto();

    CourseDto findCourseDtoById(long id);

    Course findById(long id);

    Course create(CourseDto courseDto);

    List<CourseDto> findCourseDtoByGroupId(long id);
}
