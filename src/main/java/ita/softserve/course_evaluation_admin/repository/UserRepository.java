package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users u WHERE u.email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "select u.id as id, u.first_name as first_name, u.last_name as last_name" +
            ", u.password as password, u.email as email, u.group_id  as group_id  from users u inner join user_roles ur on u.id = ur.user_id where ur.role_id = ?1 AND u.group_id is null", nativeQuery = true)
    List<User> findUsersByRoleAndGroupIsNull(int roleOrdinal);

}
