package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.dto.StudentDto;

import java.util.List;

public interface StudentService {
    List<StudentDto> findStudentsNotIncludeGroup(int roleOrdinal);

    List<StudentDto> findAllStudents(int roleOrdinal);
}
