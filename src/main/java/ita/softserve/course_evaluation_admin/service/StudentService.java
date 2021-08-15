package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    Page<StudentDto> findStudentsNotIncludeGroup(int roleOrdinal, String filter, Pageable pageable);

    Page<StudentDto> findAllStudents(int roleOrdinal, Pageable pageable);
}
