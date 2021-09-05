package ita.softserve.course_evaluation_admin.configuration;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.security.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;


public class CustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {

        User user = MockUserUtils.getUser(withMockCustomUser);
        UserDetails userDetails = UserDetailsImpl.fromUser(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, user.getEmail(), userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        return context;
    }
}
