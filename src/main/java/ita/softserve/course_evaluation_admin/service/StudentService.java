package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    List<StudentDto> findStudentsNotIncludeGroup(int roleOrdinal);

    Page<StudentDto> findAllStudents(int roleOrdinal, Pageable pageable);
}
