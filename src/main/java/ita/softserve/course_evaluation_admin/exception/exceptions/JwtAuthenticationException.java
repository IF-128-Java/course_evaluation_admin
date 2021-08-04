package ita.softserve.course_evaluation_admin.exception.exceptions;

public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String s) {
        super(s);
    }
}
