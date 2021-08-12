package ita.softserve.course_evaluation_admin.validator;

import ita.softserve.course_evaluation_admin.annotation.StartDateBeforeEndDate;
import ita.softserve.course_evaluation_admin.dto.CourseDto;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

@Component
public class StartDateBeforeEndDateValidator implements ConstraintValidator<StartDateBeforeEndDate, CourseDto> {

    @Override
    public void initialize(StartDateBeforeEndDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CourseDto courseDto, ConstraintValidatorContext context) {
        LocalDateTime startDate = courseDto.getStartDate();
        LocalDateTime endDate = courseDto.getEndDate();
        if (startDate == null || endDate == null) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("End date or start date should not be null!")
                    .addConstraintViolation();
            return false;
        }
        return startDate.isBefore(endDate);
    }
}
