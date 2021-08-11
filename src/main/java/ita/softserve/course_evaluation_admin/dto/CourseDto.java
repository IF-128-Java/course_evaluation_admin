package ita.softserve.course_evaluation_admin.dto;

import ita.softserve.course_evaluation_admin.annotation.StartDateBeforeEndDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@StartDateBeforeEndDate
public class CourseDto {
    private Long id;
    @Size(min = 3, message = "Course name should not be less than 3 !")
    private String courseName;
    @Size(min = 10, message = "Course description should not be less than 10 !")
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UserDto teacherDto;
}
