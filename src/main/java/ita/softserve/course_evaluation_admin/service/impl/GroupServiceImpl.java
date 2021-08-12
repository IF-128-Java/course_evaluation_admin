package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.GroupDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.NotEmptyGroupException;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import ita.softserve.course_evaluation_admin.repository.GroupRepository;
import ita.softserve.course_evaluation_admin.service.CourseService;
import ita.softserve.course_evaluation_admin.service.GroupService;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final CourseService courseService;

    public GroupServiceImpl(GroupRepository groupRepository, UserService userService, CourseService courseService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    public Page<GroupDto> findAllGroupDto(Pageable pageable) {
        return groupRepository.findAll(pageable).map(GroupDtoMapper::toDto);
    }

    @Override
    public Group findById(long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The group does not exist by this id: " + id));
    }

    @Override
    public GroupDto findGroupDtoById(long id) {
        return GroupDtoMapper.toDto(findById(id));
    }

    @Override
    public void deleteById(long id) {
        final Group foundGroup = findById(id);
        if (!foundGroup.getStudents().isEmpty()) {
            throw new NotEmptyGroupException("The list of students is not empty in the group with id: " + foundGroup.getId());
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
    public GroupDto addCourse(long id, CourseDto courseDto) {
        Group foundGroup = findById(id);
        Set<Course> courses = foundGroup.getCourses();
        Course foundCourse = courseService.findById(courseDto.getId());
        courses.add(foundCourse);
        foundGroup.setCourses(courses);
        return GroupDtoMapper.toDto(groupRepository.save(foundGroup));
    }

    @Override
    public Group addStudents(long groupId, List<UserDto> studentDtos) {
        final Group groupFound = findById(groupId);

        final List<User> usersFound = studentDtos
                .stream()
                .map(UserDto::getId)
                .map(userService::findById)
                .collect(Collectors.toList());

        final Optional<User> notExpectedUser = usersFound.stream()
                .filter(s -> s.getGroup() != null && s.getGroup().getId() != groupId).findFirst();
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
    public Group removeStudents(long groupId, List<UserDto> studentDtos) {
        final Group groupFound = findById(groupId);
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
