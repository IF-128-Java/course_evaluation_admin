package ita.softserve.course_evaluation_admin.exception.exceptions;

public class WrongEmailException extends RuntimeException {
    public WrongEmailException(String message) {
        super(message);
    }
}
