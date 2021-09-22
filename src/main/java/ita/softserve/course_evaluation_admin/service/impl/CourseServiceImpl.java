package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.mapper.CourseDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.repository.CourseRepository;
import ita.softserve.course_evaluation_admin.service.CourseService;
import ita.softserve.course_evaluation_admin.service.SiteNotificationService;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserService userService;
    private final SiteNotificationService siteNotificationService;

    public CourseServiceImpl(CourseRepository courseRepository, UserService userService, SiteNotificationService siteNotificationService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.siteNotificationService = siteNotificationService;
    }

    @Override
    public Page<CourseDto> findAllCourseDto(Pageable pageable) {
        return courseRepository.findAll(pageable).map(CourseDtoMapper::toDto);
    }

    @Override
    public CourseDto findCourseDtoById(long id) {
        return CourseDtoMapper.toDto(findById(id));
    }

    @Override
    public Course findById(long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The course does not exist by this id: " + id));
    }

    @Override
    public Course create(CourseDto courseDto) {
        Course course = CourseDtoMapper.fromDto(courseDto);
        User teacher = userService.findById(courseDto.getTeacherDto().getId());
        course.setTeacher(teacher);
        siteNotificationService.processCreateSiteNotification(
                "You teach a new course!",
                "Hi " + teacher.getFirstName() + ", you were attached as a teacher to the \"" + course.getCourseName() + "\" course!",
                teacher
        );
        return courseRepository.save(course);
    }

    @Override
    public Page<CourseDto> findCourseDtoByGroupId(long id, String filter, Pageable pageable, List<String> status) {
        status = status.stream().map(String::toUpperCase).collect(Collectors.toList());
        return courseRepository.findAllByGroupId(id, filter, pageable, status).map(CourseDtoMapper::toDto);
    }

    @Override
    public Page<CourseDto> findAllByFilterAndExcludeGroup(long excludeGroupId, String filter, Pageable pageable) {
        return courseRepository.findAllByFilterAndExcludeGroup(excludeGroupId, filter, pageable).map(CourseDtoMapper::toDto);
    }

    @Override
    public CourseDto editCourse(CourseDto courseDto) {
        return CourseDtoMapper.toDto(courseRepository.save(CourseDtoMapper.fromDto(courseDto)));
    }

    @Override
    public List<CourseDto> getByName(String courseName) {
        return CourseDtoMapper.toDto(courseRepository.findCourseByName(courseName));
    }

    @Override
    public void deleteById(long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("The course does not exist by id: " + id);
        }
        courseRepository.deleteById((id));

    }
}
