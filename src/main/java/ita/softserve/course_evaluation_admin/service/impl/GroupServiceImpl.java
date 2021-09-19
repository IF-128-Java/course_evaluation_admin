package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.GroupDtoMapper;
import ita.softserve.course_evaluation_admin.entity.ChatRoom;
import ita.softserve.course_evaluation_admin.entity.ChatType;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.CourseDateException;
import ita.softserve.course_evaluation_admin.exception.exceptions.GroupAlreadyExistException;
import ita.softserve.course_evaluation_admin.exception.exceptions.NotEmptyGroupException;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import ita.softserve.course_evaluation_admin.repository.GroupRepository;
import ita.softserve.course_evaluation_admin.service.ChatRoomService;
import ita.softserve.course_evaluation_admin.service.CourseService;
import ita.softserve.course_evaluation_admin.service.GroupService;
import ita.softserve.course_evaluation_admin.service.SiteNotificationService;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final CourseService courseService;
    private final ChatRoomService chatRoomService;
    private final SiteNotificationService siteNotificationService;

    public GroupServiceImpl(GroupRepository groupRepository, UserService userService, CourseService courseService, ChatRoomService chatRoomService, SiteNotificationService siteNotificationService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.courseService = courseService;
        this.chatRoomService = chatRoomService;
        this.siteNotificationService = siteNotificationService;
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
    public Group create(String name) {
        groupRepository.findByName(name)
                .ifPresent(g -> {
                    throw new GroupAlreadyExistException("The group already exist by this name: " + g.getGroupName());
                });

        return groupRepository.save(Group
                .builder()
                .groupName(name)
                .chatRoom(
                        chatRoomService.create(
                                ChatRoom.builder()
                                        .chatType(ChatType.GROUP)
                                        .build()
                        )
                )
                .build());
    }

    @Override
    public Group updateName(long id, String name) {
        Group foundGroup = findById(id);
        if (foundGroup.getGroupName().equals(name)) {
            return foundGroup;
        }
        groupRepository.findByName(name)
                .ifPresent(g -> {
                    throw new GroupAlreadyExistException("The group already exist by this name: " + g.getGroupName());
                });
        foundGroup.setGroupName(name);
        return groupRepository.save(foundGroup);
    }

    @Override
    public GroupDto addCourse(long id, CourseDto courseDto) {
        Group foundGroup = findById(id);
        final int validDaysAfterStart = 7;
        Set<Course> courses = foundGroup.getCourses();
        Course foundCourse = courseService.findById(courseDto.getId());
        if (isCourseCompleted(foundCourse)) {
            throw new CourseDateException("The selected course with name " + foundCourse.getCourseName() + " has already ended!");
        }
        if (foundCourse.getStartDate().plusDays(validDaysAfterStart).truncatedTo(ChronoUnit.DAYS).isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
            throw new CourseDateException("The selected course with name " + foundCourse.getCourseName() + " has already started more then " + validDaysAfterStart + " days ago!");
        }
        courses.add(foundCourse);
        foundGroup.setCourses(courses);
        foundGroup.getStudents().forEach(u ->
                siteNotificationService.processCreateSiteNotification(
                        "Started learning a new course!",
                        "Hi " + u.getFirstName() + ", your \"" + foundGroup.getGroupName() + "\" group started learning \""
                                + foundCourse.getCourseName() + "\" course!",
                        u.getId()
                )
        );
        return GroupDtoMapper.toDto(groupRepository.save(foundGroup));
    }

    @Override
    public GroupDto removeCourse(long id, CourseDto courseDto) {
        Group foundGroup = findById(id);
        Set<Course> courses = foundGroup.getCourses();
        Course foundCourse = courseService.findById(courseDto.getId());
        if (isCourseCompleted(foundCourse)) {
            throw new CourseDateException("The selected course with name " + foundCourse.getCourseName() + " has already completed! You cannot delete a completed course!");
        }
        courses.remove(foundCourse);
        foundGroup.setCourses(courses);
        foundGroup.getStudents().forEach(u ->
                siteNotificationService.processCreateSiteNotification(
                        "Stopped learning the course!",
                        "Hi " + u.getFirstName() + ", your \"" + foundGroup.getGroupName() + "\" group stopped learning \""
                                + foundCourse.getCourseName() + "\" course!",
                        u.getId()
                )
        );
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

        final Optional<User> userInGroup = usersFound.stream()
                .filter(s -> s.getGroup() != null && s.getGroup().getId() != groupId).findFirst();
        userInGroup.ifPresent(u -> {
            throw new WrongIdException("The user with id: " + u.getId() + " already included in the group with id: " + u.getGroup().getId());
        });
        final Optional<User> userWithOutStudentRole = usersFound.stream()
                .filter(s -> !s.getRoles().contains(Role.ROLE_STUDENT)).findFirst();
        userWithOutStudentRole.ifPresent(u -> {
            throw new WrongIdException("The user with id: " + u.getId() + " is not a student!");
        });
        usersFound.forEach(u -> u.setGroup(groupFound));
        usersFound.forEach(u ->
                siteNotificationService.processCreateSiteNotification(
                        "Added to the group!",
                        "Hi " + u.getFirstName() + ", you have been added to the \"" + groupFound.getGroupName() + "\" group!",
                        u.getId())
        );
        List<User> groupFoundStudents = groupFound.getStudents();
        groupFound.setStudents(Stream.of(groupFoundStudents, usersFound).flatMap(Collection::stream).distinct().collect(Collectors.toList()));
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
            throw new WrongIdException("The user with id: " + u.getId() + " isn't included in the group!");
        });
        usersFound.forEach(u -> u.setGroup(null));
        usersFound.forEach(u ->
                siteNotificationService.processCreateSiteNotification(
                        "Deleted from the group!",
                        "Hi " + u.getFirstName() + ", you have been deleted from the \"" + groupFound.getGroupName() + "\" group!",
                        u.getId()
                )
        );
        List<User> groupFoundStudents = groupFound.getStudents();
        groupFoundStudents.removeAll(usersFound);
        groupFound.setStudents(groupFoundStudents);
        return groupRepository.save(groupFound);
    }

    private boolean isCourseCompleted(Course course) {
        return course.getEndDate().truncatedTo(ChronoUnit.DAYS).isBefore(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS));
    }
}
