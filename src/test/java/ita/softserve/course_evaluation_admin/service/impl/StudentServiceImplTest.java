package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.repository.UserRepository;
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

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    private User leon;
    private User tony;
    private Pageable pageable;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void beforeEach() {
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

        pageable = PageRequest.of(0, 55);
    }

    @Test
    void findStudentsNotIncludeGroup() {
        List<User> students = List.of(leon, tony);
        String filter = "Name";
        Page<User> userPage = new PageImpl<>(students, pageable, students.size());
        when(userRepository.findUsersByRoleIdAndGroupIsNull(Role.ROLE_STUDENT.ordinal(), filter, pageable))
                .thenReturn(userPage);
        studentService.findStudentsNotIncludeGroup(Role.ROLE_STUDENT.ordinal(), filter, pageable);
        verify(userRepository).findUsersByRoleIdAndGroupIsNull(Role.ROLE_STUDENT.ordinal(), filter, pageable);
    }

    @Test
    void findAllStudents() {
        List<User> students = List.of(leon, tony);
        Page<User> userPage = new PageImpl<>(students, pageable, students.size());
        when(userRepository.findAllByRoleId(Role.ROLE_STUDENT.ordinal(), pageable))
                .thenReturn(userPage);
         studentService.findAllStudents(pageable);
        verify(userRepository).findAllByRoleId(Role.ROLE_STUDENT.ordinal(), pageable);
    }
}