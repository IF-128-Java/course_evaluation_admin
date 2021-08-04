package ita.softserve.course_evaluation_admin.security.jwt;

import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.exception.exceptions.JwtAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;

@Component
public class AdminRoleJwtFilter extends GenericFilterBean {
    private final JwtTokenUtils jwtTokenUtils;

    public AdminRoleJwtFilter(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenUtils.getToken((HttpServletRequest) servletRequest);
        String username;
        Set<Role> roles = null;
        if (jwtTokenUtils.validate(token)) {
            username = jwtTokenUtils.getUsername(token);
            roles = jwtTokenUtils.getRoles(username);
        }
        if (roles == null || !roles.contains(Role.ROLE_ADMIN)) {
            throw new JwtAuthenticationException("You don’t have permission to access this resource");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
