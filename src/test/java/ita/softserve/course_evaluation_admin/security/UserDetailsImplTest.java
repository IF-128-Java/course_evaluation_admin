package ita.softserve.course_evaluation_admin.security;

import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDetailsImplTest {
    private User mike;
    private List<SimpleGrantedAuthority> mikeAuthorities;

    @BeforeEach
    void setUp() {
        Set<Role> studentRoles = new HashSet<>();
        studentRoles.add(Role.ROLE_STUDENT);
        mike = User.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Bernardo")
                .email("mike@gmail.com")
                .password("super_password")
                .roles(studentRoles)
                .build();
        mikeAuthorities = studentRoles.stream()
                .map(Role::getPermissions)
                .flatMap(Collection::stream)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Test
    void fromUser() {
        org.springframework.security.core.userdetails.UserDetails expected = new org.springframework.security.core.userdetails.User(
                mike.getEmail(), mike.getPassword(), true, true, true,
                true, mikeAuthorities);
        UserDetails actual = UserDetailsImpl.fromUser(mike);
        assertEquals(expected, actual);
    }
}