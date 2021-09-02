package ita.softserve.course_evaluation_admin.security;

import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername() {
        String email = "user@gmail.com";
        String password = "password";
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        UserDetails userDetails = new UserDetailsImpl(email, password, Collections.emptyList(), true);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        try (MockedStatic<UserDetailsImpl> utilities = Mockito.mockStatic(UserDetailsImpl.class)) {
            utilities.when(() -> UserDetailsImpl.fromUser(user))
                    .thenReturn(userDetails);
            userDetailsService.loadUserByUsername(email);
            verify(userRepository).findByEmail(email);
            verifyNoMoreInteractions(userRepository);
        }
    }
}