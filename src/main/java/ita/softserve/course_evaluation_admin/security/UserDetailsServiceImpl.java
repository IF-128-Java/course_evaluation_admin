package ita.softserve.course_evaluation_admin.security;

import ita.softserve.course_evaluation_admin.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return UserDetailsImpl
                .fromUser(userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists")));
    }
}
