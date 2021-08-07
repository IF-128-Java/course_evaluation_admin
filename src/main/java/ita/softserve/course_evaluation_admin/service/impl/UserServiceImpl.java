package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.UserDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import ita.softserve.course_evaluation_admin.repository.UserRepository;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAllUserDto() {
        return UserDtoMapper.toDto(userRepository.findAll());
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new WrongIdException("The user does not exist by this id: " + id));
    }

    @Override
    public UserDto updateRoles(long user_id, Set<Role> roles) {
        final User foundUser = findById(user_id);
        foundUser.setRoles(roles);
        return UserDtoMapper.toDto(userRepository.save(foundUser));
    }

    @Override
    public List<UserDto> findUsersDtoByRoleAndGroupIsNull(int roleOrdinal) {
        return UserDtoMapper.toDto(userRepository
                .findUsersByRoleAndGroupIsNull(roleOrdinal));
    }

    @Override
    public UserDto findUserDtoById(long id) {
        return UserDtoMapper.toDto(findById(id));
    }
}
