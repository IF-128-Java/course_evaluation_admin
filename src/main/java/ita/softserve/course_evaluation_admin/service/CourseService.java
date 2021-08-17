package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    List<CourseDto> findAllCourseDto();

    CourseDto findCourseDtoById(long id);

    Course findById(long id);

    Course create(CourseDto courseDto);

    List<CourseDto> findCourseDtoByGroupId(long id);

    Page<CourseDto> findAllByFilterAndExcludeGroup(long excludeGroupId, String filter, Pageable pageable);

    CourseDto editCourse(CourseDto courseDto);

    List<CourseDto> getByName(String courseName);

    void deleteById(long id);
}
