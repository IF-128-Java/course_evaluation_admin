package ita.softserve.course_evaluation_admin.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Slf4j
@Component
public class JwtUtils {


    @Value("${jwt.secret}")
    private String jwtKey;
    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.authentication_scheme}")
    private String authenticationScheme;

    private final UserDetailsService userDetailsService;

    public JwtUtils(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @PostConstruct
    protected void init() {
        jwtKey = Base64.getEncoder().encodeToString(jwtKey.getBytes());
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
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

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
