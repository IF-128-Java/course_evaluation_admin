package ita.softserve.course_evaluation_admin.dto.user;

import ita.softserve.course_evaluation_admin.entity.User;

public class UserRolesDtoMapper {
    public static UserRolesDto toDto(User user) {
        return new UserRolesDto(user.getId(), user.getRoles());
    }
}
