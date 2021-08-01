package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
