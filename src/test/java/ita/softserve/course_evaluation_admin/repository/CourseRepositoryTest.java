package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.ChatRoom;
import ita.softserve.course_evaluation_admin.entity.ChatType;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
class CourseRepositoryTest {
    private User teacher;
    private Group if128;
    private Group lv120;
    private Course java;
    private Course web;
    private Course python;
    private PageRequest pageRequest;
    private String filter;
    private String[] status;
    private ChatRoom if128chatRoom;
    private ChatRoom lv120chatRoom;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void beforeEach() {
        if128chatRoom = ChatRoom.builder()
                .chatType(ChatType.GROUP)
                .build();
        lv120chatRoom = ChatRoom.builder()
                .chatType(ChatType.GROUP)
                .build();
        teacher = User.builder()
                .firstName("Mike")
                .lastName("Wood")
                .email("mike@com")
                .roles(Set.of(Role.ROLE_TEACHER))
                .password("password")
                .build();

        if128 = Group.builder()
                .groupName("if128")
                .chatRoom(if128chatRoom)
                .build();

        lv120 = Group.builder()
                .groupName("lv120")
                .chatRoom(lv120chatRoom)
                .build();

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
                .groups(Set.of(lv120))
                .teacher(teacher)
                .build();

        filter = "";
        pageRequest = PageRequest.of(0, 5);
        status = new String[]{"ACTIVE", "COMPLETED", "EXPECTED"};


    }


    @Test
    void findAllByGroupId() {
        chatRoomRepository.save(if128chatRoom);
        chatRoomRepository.save(lv120chatRoom);
        userRepository.save(teacher);
        groupRepository.save(if128);
        groupRepository.save(lv120);
        courseRepository.save(java);
        courseRepository.save(web);
        courseRepository.save(python);
        if128.setCourses(Set.of(java, web));
        lv120.setCourses(Set.of(python));
        Page<Course> actual = courseRepository.findAllByGroupId(if128.getId(), java.getCourseName(), pageRequest, status);

        assertTrue(actual.stream().anyMatch(e -> e.equals(java)));
        assertEquals(1, actual.getTotalElements());
    }

    @Test
    void findAllByGroupIdCheckStatus() {
        chatRoomRepository.save(if128chatRoom);
        chatRoomRepository.save(lv120chatRoom);
        userRepository.save(teacher);
        groupRepository.save(if128);
        groupRepository.save(lv120);
        courseRepository.save(java);
        courseRepository.save(web);
        courseRepository.save(python);
        if128.setCourses(Set.of(java, web));
        lv120.setCourses(Set.of(python));
        status = new String[]{"ACTIVE", "COMPLETED"};
        Page<Course> actual = courseRepository.findAllByGroupId(if128.getId(), filter, pageRequest, status);

        assertTrue(actual.stream().anyMatch(e -> e.equals(java)));
        assertTrue(actual.stream().anyMatch(e -> e.equals(web)));
        assertEquals(2, actual.getTotalElements());

        status = new String[]{"EXPECTED"};
        actual = courseRepository.findAllByGroupId(if128.getId(), filter, pageRequest, status);
        assertEquals(0, actual.getTotalElements());

        actual = courseRepository.findAllByGroupId(lv120.getId(),filter,pageRequest, status);
        assertEquals(1, actual.getTotalElements());
    }

    @Test
    void findAllByNotExistingGroupId() {
        final Page<Course> actual = courseRepository.findAllByGroupId(1, filter, pageRequest, status);
        assertTrue(actual.isEmpty());
    }
}
