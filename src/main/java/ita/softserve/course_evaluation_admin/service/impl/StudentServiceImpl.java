package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.StudentDto;
import ita.softserve.course_evaluation_admin.dto.mapper.StudentDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.repository.UserRepository;
import ita.softserve.course_evaluation_admin.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    private final UserRepository userRepository;

    public StudentServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<StudentDto> findStudentsNotIncludeGroup(int roleOrdinal, String filter, Pageable pageable) {
        return userRepository
                .findUsersByRoleIdAndGroupIsNull(roleOrdinal, filter, pageable).map(StudentDtoMapper::toDto);
    }

    @Override
    public Page<StudentDto> findAllStudents(Pageable pageable) {
        return userRepository
                .findAllByRoleId(Role.ROLE_STUDENT.ordinal(), pageable).map(StudentDtoMapper::toDto);
    }
}
