package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
class UserRepositoryTest {
    private User mike;
    private User leon;
    private User tony;
    private User leo;
    private Group group;
    private Pageable pageable;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    void beforeEach() {

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

        leo = User.builder()
                .firstName("Leo")
                .lastName("Kio")
                .email("leo@com")
                .roles(Collections.emptySet())
                .password("password")
                .build();

        group = Group.builder()
                .groupName("IF128")
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
        Group savedGroup = groupRepository.save(group);
        leon.setGroup(savedGroup);

        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        group.setStudents(List.of(mike));

        List<User> studentsActual = userRepository
                .findUsersByRoleIdAndGroupIsNull(Role.ROLE_STUDENT.ordinal(), "", pageable)
                .getContent();
        List<User> studentExpected = List.of(tony);

        assertEquals(studentExpected.size(), studentsActual.size());
        assertTrue(studentsActual.contains(tony));
    }

    @Test
    void findUsersByRoleIdAndGroupIsNullCheckFilter() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);

        List<User> studentsActual = userRepository
                .findUsersByRoleIdAndGroupIsNull(Role.ROLE_STUDENT.ordinal(), leon.getLastName(), pageable)
                .getContent();
        List<User> studentExpected = List.of(leon);

        assertEquals(studentExpected.size(), studentsActual.size());
        assertTrue(studentsActual.contains(leon));
    }

    @Test
    void findAllByRoleId() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);

        Page<User> studentsPage = userRepository
                .findAllByRoleId(Role.ROLE_STUDENT.ordinal(), pageable);
        Page<User> teachersPage = userRepository
                .findAllByRoleId(Role.ROLE_TEACHER.ordinal(), pageable);

        List<User> studentsActual = studentsPage.getContent();
        List<User> teachersActual = teachersPage.getContent();

        List<User> studentsExpected = List.of(leon, tony);
        List<User> teachersExpected = List.of(mike);

        assertEquals(studentsExpected.size(), studentsActual.size());
        assertEquals(teachersExpected.size(), teachersActual.size());

        assertTrue(studentsActual.contains(leon));
        assertTrue(studentsActual.contains(tony));
        assertTrue(teachersActual.contains(mike));
    }

    @Test
    void findEmptyListByRoleId() {
        long actualTotalElements = userRepository
                .findAllByRoleId(Role.ROLE_STUDENT.ordinal(), pageable)
                .getTotalElements();

        assertEquals(0, actualTotalElements);
    }

    @Test
    void findAll() {
       userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        String filter = "";
        List<User> studentsActual = userRepository
                .findAll(filter, new Integer[]{0,1,2},pageable)
                .getContent();
        List<User> studentExpected = List.of(leon, mike, tony);
        System.out.println(studentsActual);
        assertEquals(studentExpected.size(), studentsActual.size());
        assertTrue(studentsActual.contains(leon));
        assertTrue(studentsActual.contains(mike));
        assertTrue(studentsActual.contains(tony));
    }

    @Test
    void findAllCheckSearch() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        String filter = "on";
        List<User> studentsActual = userRepository
                .findAll(filter, new Integer[]{0,1,2}, pageable)
                .getContent();
        List<User> studentExpected = List.of(leon, tony);

        assertEquals(studentExpected.size(), studentsActual.size());
        assertTrue(studentsActual.contains(leon));
        assertTrue(studentsActual.contains(tony));
    }

    @Test
    void findAllCheckFilterStudents() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        String filter = "";
        List<User> studentsActual = userRepository
                .findAll(filter, new Integer[]{0}, pageable)
                .getContent();
        List<User> studentExpected = List.of(leon, tony);

        assertEquals(studentExpected.size(), studentsActual.size());
        assertTrue(studentsActual.contains(leon));
        assertTrue(studentsActual.contains(tony));
    }
    @Test
    void findAllCheckFilterTeacher() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        String filter = "";
        List<User> studentsActual = userRepository
                .findAll(filter, new Integer[]{1}, pageable)
                .getContent();
        List<User> studentExpected = List.of(mike);

        assertEquals(studentExpected.size(), studentsActual.size());
        assertTrue(studentsActual.contains(mike));
    }
    @Test
    void findAllCheckFilterAdmin() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        String filter = "";
        List<User> studentsActual = userRepository
                .findAll(filter, new Integer[]{2}, pageable)
                .getContent();
        List<User> studentExpected = List.of(tony);

        assertEquals(studentExpected.size(), studentsActual.size());
        assertTrue(studentsActual.contains(tony));
    }
    @Test
    void findAllNotMaches() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        String filter = "not existing name";
        Integer[] roles = {0};
        List<User> studentsActual = userRepository
                .findAll(filter,roles, pageable)
                .getContent();
        assertTrue(studentsActual.isEmpty());
    }

    @Test
    void findAllRoleNull() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        userRepository.save(leo);
        String filter = "";
        List<User> actual = userRepository
                .findAllRoleNull(filter, pageable)
                .getContent();
        List<User> expected = List.of(this.leo);
        assertEquals(expected.size(),actual.size());
        assertTrue(actual.contains(leo));
    }
    @Test
    void findAllRoleNullAndCheckSearch() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        userRepository.save(leo);
        String filter = "Le";
        List<User> actual = userRepository
                .findAllRoleNull(filter, pageable)
                .getContent();
        List<User> expected = List.of(this.leo);
        assertEquals(expected.size(),actual.size());
        assertTrue(actual.contains(leo));
    }
    @Test
    void findAllRoleNullEmpty() {
        userRepository.save(mike);
        userRepository.save(leon);
        userRepository.save(tony);
        String filter = "";
        List<User> studentsActual = userRepository
                .findAllRoleNull(filter, pageable)
                .getContent();
        assertTrue(studentsActual.isEmpty());
    }
}
