package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.user.UserDto;
import ita.softserve.course_evaluation_admin.dto.user.UserRolesDto;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongEmailException;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import ita.softserve.course_evaluation_admin.repository.UserRepository;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

import static ita.softserve.course_evaluation_admin.dto.user.UserDtoMapper.fromDto;
import static ita.softserve.course_evaluation_admin.dto.user.UserDtoMapper.toDto;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAll() {
        return toDto(userRepository.findAll());
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new WrongIdException("The user does not exist by this id: " + id));
    }

    @Override
    public User updateRoles(UserRolesDto dto) {
        User user = findById(dto.getId());
        user.setRoles(dto.getRoles());
        return userRepository.save(user);
    }


}
