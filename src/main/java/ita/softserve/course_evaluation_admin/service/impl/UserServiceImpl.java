package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.UserDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.UserRoleException;
import ita.softserve.course_evaluation_admin.repository.UserRepository;
import ita.softserve.course_evaluation_admin.service.SiteNotificationService;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SiteNotificationService siteNotificationService;

    public UserServiceImpl(UserRepository userRepository, SiteNotificationService siteNotificationService) {
        this.userRepository = userRepository;
        this.siteNotificationService = siteNotificationService;
    }

    @Override
    public Page<UserDto> findAllUserDto(String search, Integer[] roles, Pageable pageable) {
        return roles.length == 0 ? userRepository.findAllRoleNull(search, pageable).map(UserDtoMapper::toDto)
                : userRepository.findAll(search, roles, pageable).map(UserDtoMapper::toDto);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The user does not exist by this id: " + id));
    }

    @Override
    public UserDto updateRoles(long userId, Set<Role> roles) {
        final User foundUser = findById(userId);
        if (roles.isEmpty()) {
            throw new UserRoleException("The user must have at least one role!");
        }
        Group group = foundUser.getGroup();
        if (group != null && !roles.contains(Role.ROLE_STUDENT)) {
            throw new UserRoleException("The user is a student and enrolled in the group with name: " + group.getGroupName());
        }
        Set<Course> teachCourses = foundUser.getTeachCourses();
        if (teachCourses != null && !teachCourses.isEmpty() && !roles.contains(Role.ROLE_TEACHER)) {
            throw new UserRoleException("The user is a teacher in the course with name: " +
                    teachCourses.stream().findFirst().orElseThrow().getCourseName());
        }

        roles.forEach(r -> {
                    if (!foundUser.getRoles().contains(r))
                        siteNotificationService.processCreateSiteNotification(
                                "You've been given a new role!",
                                "Hi " + foundUser.getFirstName() + ", we are glad to announce that you are now a " + r.name().substring(5) + "!",
                                foundUser
                        );
                }
        );
        foundUser.getRoles().forEach(r -> {
                    if (!roles.contains(r))
                        siteNotificationService.processCreateSiteNotification(
                                "The role was taken away from you!",
                                "Hi " + foundUser.getFirstName() + ", unfortunately you are no longer a " + r.name().substring(5) + "!",
                                foundUser
                        );
                }
        );

        foundUser.setRoles(roles);
        return UserDtoMapper.toDto(userRepository.save(foundUser));
    }

    @Override
    public UserDto findUserDtoById(long id) {
        return UserDtoMapper.toDto(findById(id));
    }
}
