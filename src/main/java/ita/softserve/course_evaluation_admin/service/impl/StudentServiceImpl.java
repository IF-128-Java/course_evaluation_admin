package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.StudentDto;
import ita.softserve.course_evaluation_admin.dto.mapper.StudentDtoMapper;
import ita.softserve.course_evaluation_admin.repository.UserRepository;
import ita.softserve.course_evaluation_admin.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StudentServiceImpl implements StudentService {
    private final UserRepository userRepository;

    public StudentServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<StudentDto> findStudentsNotIncludeGroup(int roleOrdinal) {
        return StudentDtoMapper.toDto(userRepository
                .findUsersByRoleIdAndGroupIsNull(roleOrdinal));
    }

    @Override
    public Page<StudentDto> findAllStudents(int roleOrdinal, Pageable pageable) {
        return userRepository
                .findAllByRoleId(roleOrdinal,pageable).map(StudentDtoMapper::toDto);
    }
}
