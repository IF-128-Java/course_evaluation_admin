package ita.softserve.course_evaluation_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {
    private Long id;
    private String courseName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UserDto teacherDto;
}
