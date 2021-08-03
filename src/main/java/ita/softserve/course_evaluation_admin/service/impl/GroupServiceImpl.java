package ita.softserve.course_evaluation_admin.service.impl;


import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import ita.softserve.course_evaluation_admin.repository.GroupRepository;
import ita.softserve.course_evaluation_admin.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group findById(long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new WrongIdException("The group does not exist by this id: " + id));
    }

    @Override
    public Group addStudents(Group group, List<User> students) {
        final Optional<User> notExpectedUser = students.stream()
                .filter(s -> s.getGroup() != group && s.getGroup() != null).findFirst();
        notExpectedUser.ifPresent(u -> {
            throw new WrongIdException("The user with id: " + u.getId() + " already included in the group with id: " + u.getGroup().getId());
        });
        List<User> studentList = students
                .stream()
                .peek(u -> u.setGroup(group)).collect(Collectors.toList());
        group.setStudents(studentList);
        return groupRepository.save(group);
    }

    @Override
    public Group removeStudents(Group group, List<User> students) {
        final Optional<User> notExpectedUser = students.stream()
                .filter(s -> s.getGroup() == null || !s.getGroup().equals(group)).findFirst();
        notExpectedUser.ifPresent(u -> {
            throw new WrongIdException("The user with id: " + u.getId() + " isn't included in the group");
        });
        List<User> studentList = students
                .stream()
                .peek(u -> u.setGroup(null)).collect(Collectors.toList());
        group.setStudents(studentList);
        return groupRepository.save(group);
    }

    @Override
    public Group create(String groupName) {
        return groupRepository.save(Group
                .builder()
                .groupName(groupName)
                .build());
    }
}
