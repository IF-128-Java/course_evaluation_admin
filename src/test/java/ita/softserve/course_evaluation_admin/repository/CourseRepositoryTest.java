package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
class CourseRepositoryTest {
    private User teacher;
    private Group group;
    private Course course;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    void beforeEach() {
        teacher = User.builder()
                .firstName("Mike")
                .lastName("Wood")
                .email("mike@com")
                .roles(Set.of(Role.ROLE_TEACHER))
                .password("password")
                .build();
        group = Group.builder()
                .groupName("TestGroup")
                .build();

        course = Course.builder()
                .courseName("TestCourse")
                .description("Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .groups(Set.of(group))
                .teacher(teacher)
                .build();
    }


    @Test
    void findAllByGroupId() {
        userRepository.save(teacher);
        groupRepository.save(group);
        courseRepository.save(course);
        group.setCourses(Set.of(course));
        List<Course> actual = courseRepository.findAllByGroupId(group.getId());

        assertTrue(actual.contains(course));
        assertEquals(1, actual.size());
    }

    @Test
    void findAllByNotExistingGroupId() {
        final List<Course> actual = courseRepository.findAllByGroupId(1L);
        assertTrue(actual.isEmpty());
    }
}
