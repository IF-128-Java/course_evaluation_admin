package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;

import java.util.List;

public interface GroupService {
    List<Group> findAll();

    Group findById(long id);

    Group addStudents(Group group, List<User> users);

    Group removeStudents(Group group, List<User> users);

    void delete(Group group);

    Group create(String userName);
}
