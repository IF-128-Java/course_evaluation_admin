package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.mapper.CourseDtoMapper;
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
    public List<CourseDto> findAll() {
        return CourseDtoMapper.toDto(courseRepository.findAll());
    }

    @Override
    public CourseDto findById(long id) {
        return CourseDtoMapper.toDto(courseRepository.findById(id)
                .orElseThrow(() -> new WrongIdException("The course does not exist by this id: " + id)));
    }

    @Override
    public CourseDto create(CourseDto courseDto) {
        return CourseDtoMapper.toDto(courseRepository.save(CourseDtoMapper.fromDto(courseDto)));
    }
}
