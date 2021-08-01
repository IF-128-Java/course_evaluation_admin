package ita.softserve.course_evaluation_admin.dto.user;

import ita.softserve.course_evaluation_admin.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesDto {

    private Long id;

    private Set<Role> roles;
}
