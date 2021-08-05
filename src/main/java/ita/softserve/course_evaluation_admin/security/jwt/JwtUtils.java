package ita.softserve.course_evaluation_admin.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Set;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String jwtKey;
    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.authentication_scheme}")
    private String authenticationScheme;

    private final UserService userService;

    public JwtTokenUtils(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        jwtKey = Base64.getEncoder().encodeToString(jwtKey.getBytes());
    }

    public boolean validate(String token) {
        if (token == null) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getToken(HttpServletRequest req) {
        String token = req.getHeader(header);
        if (token != null && token.startsWith(authenticationScheme)) {
            return token.substring(authenticationScheme.length());
        }
        return null;
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Set<Role> getRoles(String username) {
        return userService.findByEmail(username).getRoles();
    }
}
