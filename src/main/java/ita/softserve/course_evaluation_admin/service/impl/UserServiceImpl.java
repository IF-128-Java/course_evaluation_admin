package ita.softserve.course_evaluation_admin.service.impl;

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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new WrongIdException("The user does not exist by this id: " + id));
    }

    @Override
    public User updateRoles(User user, Set<Role> roles) {
        user.setRoles(roles);
        return userRepository.save(user);
    }
}
