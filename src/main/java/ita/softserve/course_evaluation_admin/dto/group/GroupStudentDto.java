package ita.softserve.course_evaluation_admin.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupStudentDto {
    private Long id;
    private String groupName;
    private List<Long> studentIds=new ArrayList<>();
}
