package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.mapper.CourseDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import ita.softserve.course_evaluation_admin.repository.CourseRepository;
import ita.softserve.course_evaluation_admin.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseDto> findAllCourseDto() {
        return CourseDtoMapper.toDto(courseRepository.findAll());
    }

    @Override
    public CourseDto findCourseDtoById(long id) {
        return CourseDtoMapper.toDto(findById(id));
    }

    @Override
    public Course findById(long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new WrongIdException("The course does not exist by this id: " + id));
    }

    @Override
    public Course create(CourseDto courseDto) {
        return courseRepository.save(CourseDtoMapper.fromDto(courseDto));
    }

    @Override
    public List<CourseDto> findCourseDtoByGroupId(long id) {
        return CourseDtoMapper.toDto(courseRepository.findAllByGroupId(id));
    }
}
