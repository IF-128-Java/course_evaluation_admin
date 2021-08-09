package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class UserRepositoryTest {
    private User mike;
    private User leon;
    private User tony;
    private Pageable pageable;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeAll
    public void beforeAll() {

        mike = User.builder()
                .firstName("Mike")
                .lastName("Wood")
                .email("mike@com")
                .roles(Set.of(Role.ROLE_TEACHER))
                .password("password")
                .build();

        leon = User.builder()
                .firstName("Leon")
                .lastName("Spinks")
                .email("Leon@com")
                .roles(Set.of(Role.ROLE_STUDENT))
                .password("password")
                .build();

        tony = User.builder()
                .firstName("Tony")
                .lastName("Bellew")
                .email("Tony@com")
                .roles(Set.of(Role.ROLE_STUDENT, Role.ROLE_ADMIN))
                .password("password")
                .build();

        pageable = PageRequest.of(0, 25);
    }

    @Test
    void findByEmail() {
        User mike = userRepository.save(this.mike);
        Optional<User> foundUser = userRepository.findByEmail(this.mike.getEmail());

        assertTrue(foundUser.isPresent());
        assertEquals(mike, foundUser.get());
    }

    @Test
    void findByNotExistingEmail() {
        assertFalse(userRepository.findByEmail("not_existing@com").isPresent());
    }

    @Test
    void findUsersByRoleIdAndGroupIsNull() {
        Group group = groupRepository.save(Group.builder().groupName("Java").build());
        leon.setGroup(group);

        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        group.setStudents(List.of(mike));

        List<User> students = userRepository.findUsersByRoleIdAndGroupIsNull(Role.ROLE_STUDENT.ordinal());

        assertEquals(1, students.size());
        assertTrue(students.contains(tony));
        leon.setGroup(null);
    }

    @Test
    void findAllByRoleId() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);

        Page<User> students = userRepository.findAllByRoleId(Role.ROLE_STUDENT.ordinal(), pageable);
        Page<User> teachers = userRepository.findAllByRoleId(Role.ROLE_TEACHER.ordinal(), pageable);

        assertEquals(2, students.getTotalElements());
        assertEquals(1, teachers.getTotalElements());
    }

    @Test
    void findEmptyListByRoleId() {
        Page<User> students = userRepository.findAllByRoleId(Role.ROLE_STUDENT.ordinal(), pageable);
        assertEquals(0, students.getTotalElements());
    }
}
