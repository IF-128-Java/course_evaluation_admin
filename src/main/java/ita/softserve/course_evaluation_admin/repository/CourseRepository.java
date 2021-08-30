package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT c.id, c.course_name, c.description, c.start_date, c.end_date, " +
            "c.teacher_id "+
            "FROM course c INNER JOIN course_group cg ON c.id = cg.course_id INNER JOIN users u ON u.id = c.teacher_id " +
            "WHERE cg.group_id = :id and lower(concat(c.course_name,' ', u.first_name,' ', u.last_name)) like concat('%',:filter,'%') " +
            "AND (CASE WHEN c.end_date <= NOW() THEN 'COMPLETED' WHEN c.start_date > NOW() THEN 'EXPECTED' WHEN c.start_date <= NOW() AND c.end_date > NOW() THEN 'ACTIVE' END) in (:status)"
            ,countQuery = "SELECT COUNT(c.id) FROM course c INNER JOIN course_group cg ON c.id = cg.course_id INNER JOIN users u ON u.id = c.teacher_id WHERE cg.group_id = :id and lower(concat(c.course_name,' ', u.first_name,' ', u.last_name)) like concat('%',:filter,'%') " +
            "AND (CASE WHEN c.end_date <= NOW() THEN 'COMPLETED' WHEN c.start_date > NOW() THEN 'EXPECTED' WHEN c.start_date <= NOW() AND c.end_date > NOW() THEN 'ACTIVE' END) in (:status)"
            , nativeQuery = true)
    Page<Course> findAllByGroupId(long id, String filter, Pageable pageable, List<String> status);

    @Query(value = "SELECT DISTINCT c.id, c.course_name, c.description, c.start_date, c.end_date, c.teacher_id FROM course c " +
            "LEFT JOIN course_group cg ON c.id = cg.course_id  INNER JOIN users u ON u.id = c.teacher_id " +
            "WHERE (cg.group_id != ?1 OR cg.group_id IS NULL) and (concat(c.course_name,' ', u.first_name,' ', u.last_name) like concat('%',?2,'%')) AND c.id NOT IN " +
            "(SELECT course_id AS id FROM course_group WHERE group_id = ?1) AND c.end_date > NOW() AND c.start_date >= (NOW() - interval '7 day')"
             ,countQuery = "SELECT COUNT(DISTINCT c.id) FROM course c " +
            "LEFT JOIN course_group cg ON c.id = cg.course_id  INNER JOIN users u ON u.id = c.teacher_id " +
            "WHERE (cg.group_id != ?1 OR cg.group_id IS NULL) and (concat(c.course_name,' ', u.first_name,' ', u.last_name) like concat('%',?2,'%')) AND c.id NOT IN " +
            "(SELECT course_id AS id FROM course_group WHERE group_id = ?1) AND c.end_date > NOW() AND c.start_date >= (NOW() - interval '7 day')"
            , nativeQuery = true)
    Page<Course> findAllByFilterAndExcludeGroup(long excludeGroupId, String filter, Pageable pageable);

    @Query(value = "SELECT c.id, course_name, description, start_date, end_date, teacher_id FROM course c " +
            "LEFT JOIN users u ON c.teacher_id = u.id WHERE course_name LIKE %:courseName%", nativeQuery = true)
    List<Course> findCourseByName(@Param("courseName") String courseName);
}
