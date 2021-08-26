package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users u WHERE u.email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u.id, u.first_name, u.last_name, u.password, u.email, u.group_id  FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id WHERE ur.role_id = ?1 AND u.group_id IS NULL AND (CONCAT(u.first_name,' ', u.last_name) like concat('%',?2,'%'))"
            , countQuery = "SELECT COUNT(u.id) FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id WHERE ur.role_id = ?1 AND u.group_id IS NULL AND (CONCAT(u.first_name,' ', u.last_name) like concat('%',?2,'%'))"
            , nativeQuery = true)
    Page<User> findUsersByRoleIdAndGroupIsNull(int roleOrdinal, String filter, Pageable pageable);

    @Query(value = "SELECT u.id, u.first_name, u.last_name" +
            ", u.password, u.email, u.group_id  FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id WHERE ur.role_id = ?1 ORDER BY u.group_id"
            , countQuery = "SELECT count(*) FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id WHERE ur.role_id = ?1"
            , nativeQuery = true)
    Page<User> findAllByRoleId(int roleOrdinal, Pageable pageable);

    @Query(value = "SELECT DISTINCT u.id, u.first_name, u.last_name, u.password, u.email, u.group_id  FROM users u " +
            "LEFT JOIN user_roles r ON u.id = r.user_id WHERE (CONCAT(u.first_name,' ', u.last_name) like concat('%',:search,'%')) " +
            "AND r.role_id IN (:roles) "
            , countQuery = "SELECT COUNT(DISTINCT u.id) FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE (CONCAT(u.first_name,' ', u.last_name) like concat('%',:search,'%')) AND r.role_id in (:roles)"
            , nativeQuery = true)
    Page<User> findAll(String search, Integer[] roles, Pageable pageable);

    @Query(value = "SELECT u.id, u.first_name, u.last_name, u.password, u.email, u.group_id  FROM users u " +
            "LEFT JOIN user_roles r ON u.id = r.user_id WHERE (CONCAT(u.first_name,' ', u.last_name) like concat('%',:search,'%')) " +
            "AND r.role_id IS NULL "
            , countQuery = "SELECT COUNT(u.id) FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE (CONCAT(u.first_name,' ', u.last_name) like concat('%',:search,'%')) AND r.role_id IS NULL"
            , nativeQuery = true)
    Page<User> findAllRoleNull(String search, Pageable pageable);
}
