package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.repository.CourseRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private String[] status;
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        User teacher = User.builder()
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
                .courseName("java")
                .description("Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .groups(Set.of(if128))
                .teacher(teacher)
                .build();
        web = Course.builder()
                .courseName("web")
                .description("Description")
                .startDate(LocalDateTime.now().minusDays(20))
                .endDate(LocalDateTime.now().minusDays(2))
                .groups(Set.of(if128))
                .teacher(teacher)
                .build();

        python = Course.builder()
                .courseName("python")
                .description("Description")
                .startDate(LocalDateTime.now().plusDays(20))
                .endDate(LocalDateTime.now().plusDays(30))
                .groups(Set.of(if128))
                .teacher(teacher)
                .build();
        status = new String[]{"ACTIVE", "COMPLETED", "EXPECTED"};
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
}