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

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private User mike;
    private User leon;
    private User tony;
    private Pageable pageable;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
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
    void findAllUserDto() {
        List<User> users = List.of(leon, tony, mike);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        userService.findAllUserDto(pageable);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void updateRoles() {
        mike.setId(2L);
        when(userRepository.findById(mike.getId())).thenReturn(Optional.ofNullable(mike));
        when(userRepository.save(mike)).thenReturn(mike);
        Set<Role> roles = Set.of(Role.ROLE_STUDENT, Role.ROLE_TEACHER);
        userService.updateRoles(mike.getId(), roles);
        verify(userRepository).save(mike);
    }

    @Test
    void updateRolesExpectException() {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        Set<Role> roles = Set.of(Role.ROLE_STUDENT, Role.ROLE_TEACHER);
        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class, () -> userService.updateRoles(id, roles));
        verify(userRepository, never()).save(mike);
        assertEquals( "The user does not exist by this id: " + id,actual.getMessage());
    }

    @Test
    void findUserDtoById() {
        mike.setId(2L);
        when(userRepository.findById(mike.getId())).thenReturn(Optional.ofNullable(mike));
        userService.findUserDtoById(mike.getId());
        verify(userRepository).findById(mike.getId());
    }

    @Test
    void findUserDtoByIdExpectException() {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class, () -> userService.findUserDtoById(id));
        verify(userRepository).findById(id);
        assertEquals("The user does not exist by this id: " + id, actual.getMessage());
    }
}