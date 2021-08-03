package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<User> findAll();

    User findById(long id);

    User updateRoles(User user, Set<Role> roles);
}
