package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query(value = "SELECT * FROM groups WHERE group_name = ?1",nativeQuery = true)
    Optional<Group> findByName(String name);
}
