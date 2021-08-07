package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.GroupDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import ita.softserve.course_evaluation_admin.repository.GroupRepository;
import ita.softserve.course_evaluation_admin.service.GroupService;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;

    public GroupServiceImpl(GroupRepository groupRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    @Override
    public List<GroupDto> findAllUserDto() {
        return GroupDtoMapper.toDto(groupRepository.findAll());
    }

    @Override
    public Group findById(long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new WrongIdException("The group does not exist by this id: " + id));
    }

    @Override
    public GroupDto findGroupDtoById(long id) {
        return GroupDtoMapper.toDto(findById(id));
    }

    @Override
    public void deleteById(long id) {
        final Group foundGroup = findById(id);
        if (!foundGroup.getStudents().isEmpty()) {
            throw new WrongIdException("The list of students is not empty in the group with id: " + foundGroup.getId());
        }
        groupRepository.deleteById(id);
    }

    @Override
    public Group create(String groupName) {
        return groupRepository.save(Group
                .builder()
                .groupName(groupName)
                .build());
    }

    @Override
    public Group addStudents(long group_id, List<UserDto> studentDtos) {
        final Group groupFound = findById(group_id);

        final List<User> usersFound = studentDtos
                .stream()
                .map(UserDto::getId)
                .map(userService::findById)
                .collect(Collectors.toList());

        final Optional<User> notExpectedUser = usersFound.stream()
                .filter(s -> s.getGroup() != null && s.getGroup().getId() != group_id).findFirst();
        notExpectedUser.ifPresent(u -> {
            throw new WrongIdException("The user with id: " + u.getId() + " already included in the group with id: " + u.getGroup().getId());
        });
        List<User> studentList = usersFound
                .stream()
                .peek(u -> u.setGroup(groupFound)).collect(Collectors.toList());
        groupFound.setStudents(studentList);
        return groupRepository.save(groupFound);
    }

    @Override
    public Group removeStudents(long group_id, List<UserDto> studentDtos) {
        final Group groupFound = findById(group_id);
        final List<User> usersFound = studentDtos
                .stream()
                .map(UserDto::getId)
                .map(userService::findById)
                .collect(Collectors.toList());
        final Optional<User> notExpectedUser = usersFound.stream()
                .filter(s -> s.getGroup() == null || !s.getGroup().equals(groupFound)).findFirst();
        notExpectedUser.ifPresent(u -> {
            throw new WrongIdException("The user with id: " + u.getId() + " isn't included in the group");
        });
        List<User> studentList = usersFound
                .stream()
                .peek(u -> u.setGroup(null)).collect(Collectors.toList());
        groupFound.setStudents(studentList);
        return groupRepository.save(groupFound);
    }
}
