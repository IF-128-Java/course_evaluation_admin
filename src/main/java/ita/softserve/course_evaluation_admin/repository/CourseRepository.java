package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT c.id AS id, c.course_name AS course_name, c.description AS description, c.start_date AS start_date, c.end_date AS end_date, " +
            "c.teacher_id AS teacher_id FROM course c INNER JOIN course_group cg ON c.id = cg.course_id WHERE cg.group_id = ?1", nativeQuery = true)
    List<Course> findAllByGroupId(long id);
}
