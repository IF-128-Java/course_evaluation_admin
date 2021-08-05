package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.CourseDto;

import java.util.List;

public interface CourseService {
    List<CourseDto> findAll();

    CourseDto findById(long id);

    CourseDto create(CourseDto courseDto);
}
