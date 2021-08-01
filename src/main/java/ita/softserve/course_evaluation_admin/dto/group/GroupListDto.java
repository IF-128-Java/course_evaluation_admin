package ita.softserve.course_evaluation_admin.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupListDto {
    private Long id;
    private String groupName;
}
