package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.mapper.CourseDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.repository.CourseRepository;
import ita.softserve.course_evaluation_admin.service.SiteNotificationService;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    private Pageable pageable;
    private Course java;
    private Course web;
    private Course python;
    private Group if128;
    private User teacher;
    private List<String> status;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserService userService;
    @Mock
    private SiteNotificationService siteNotificationsService;
    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        teacher = User.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Wood")
                .email("mike@com")
                .roles(Set.of(Role.ROLE_TEACHER))
                .password("password")
                .build();

        if128 = Group.builder()
                .id(1L)
                .groupName("if128")
                .build();
        pageable = PageRequest.of(0, 5);
        java = Course.builder()
                .id(1L)
                .courseName("java")
                .description("Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .groups(Set.of(if128))
                .teacher(teacher)
                .build();
        web = Course.builder()
                .id(2L)
                .courseName("web")
                .description("Description")
                .startDate(LocalDateTime.now().minusDays(20))
                .endDate(LocalDateTime.now().minusDays(2))
                .groups(Set.of(if128))
                .teacher(teacher)
                .build();

        python = Course.builder()
                .id(3L)
                .courseName("python")
                .description("Description")
                .startDate(LocalDateTime.now().plusDays(20))
                .endDate(LocalDateTime.now().plusDays(30))
                .groups(Set.of(if128))
                .teacher(teacher)
                .build();
        status = List.of("ACTIVE", "COMPLETED", "EXPECTED");
    }

    @Test
    void findCourseDtoByGroupId() {
        Long id = if128.getId();
        List<Course> courses = List.of(java, web, python);
        Page<Course> coursePage = new PageImpl<>(courses, pageable, courses.size());
        when(courseRepository.findAllByGroupId(id, "", pageable, status)).thenReturn(coursePage);
        courseService.findCourseDtoByGroupId(id, "", pageable, status);
        verify(courseRepository).findAllByGroupId(id, "", pageable, status);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void findById() {
        Long id = java.getId();
        when(courseRepository.findById(id)).thenReturn(Optional.ofNullable(java));
        courseService.findById(id);
        verify(courseRepository).findById(id);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void findByIdNotExisting() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> courseService.findById(1L));
        verify(courseRepository).findById(anyLong());
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void create() {
        when(userService.findById(anyLong())).thenReturn(teacher);
        when(courseRepository.save(any(Course.class))).thenReturn(any(Course.class));
        courseService.create(CourseDtoMapper.toDto(java));
        verify(courseRepository).save(any(Course.class));
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void findAllCourseDto() {
        List<Course> courseList = List.of(java, web);
        List<CourseDto> expected = CourseDtoMapper.toDto(courseList);
        Page<Course> coursePage = new PageImpl<>(courseList, pageable, courseList.size());
        when(courseRepository.findAll(pageable)).thenReturn(coursePage);
        Page<CourseDto> actual = courseService.findAllCourseDto(pageable);
        assertTrue(expected.containsAll(actual.getContent()));
        assertEquals(expected.size(), actual.getTotalElements());
        verify(courseRepository).findAll(pageable);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void findCourseDtoById() {
        Long id = java.getId();
        CourseDto expected = CourseDtoMapper.toDto(java);
        when(courseRepository.findById(id)).thenReturn(Optional.ofNullable(java));
        CourseDto actual = courseService.findCourseDtoById(id);
        assertEquals(expected, actual);
        verify(courseRepository).findById(id);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void findCourseDtoByIdNotExisting() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> courseService.findCourseDtoById(1L));
        verify(courseRepository).findById(anyLong());
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void findAllByFilterAndExcludeGroup() {
        long excludeGroupId = 1;
        String filter = "";
        List<Course> courseList = List.of(java, web);
        List<CourseDto> expected = CourseDtoMapper.toDto(courseList);
        Page<Course> coursePage = new PageImpl<>(courseList, pageable, courseList.size());
        when(courseRepository.findAllByFilterAndExcludeGroup(excludeGroupId, filter, pageable)).thenReturn(coursePage);
        Page<CourseDto> actual = courseService.findAllByFilterAndExcludeGroup(excludeGroupId, filter, pageable);
        assertTrue(expected.containsAll(actual.getContent()));
        assertEquals(expected.size(), actual.getTotalElements());
        verify(courseRepository).findAllByFilterAndExcludeGroup(excludeGroupId, filter, pageable);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void editCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(java);
        courseService.editCourse(CourseDtoMapper.toDto(java));
        verify(courseRepository).save(any(Course.class));
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void getByName() {
        when(courseRepository.findCourseByName(anyString())).thenReturn(anyList());
        courseService.getByName("java");
        verify(courseRepository).findCourseByName(anyString());
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void deleteById() {
        when(courseRepository.existsById(anyLong())).thenReturn(true);
        courseService.deleteById(1);
        verify(courseRepository).existsById(anyLong());
        verify(courseRepository).deleteById(anyLong());
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void deleteByNotExistingId() {
        when(courseRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> courseService.deleteById(1L));
        verify(courseRepository).existsById(anyLong());
        verify(courseRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(courseRepository);
    }
}