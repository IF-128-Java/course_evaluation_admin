package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.SiteNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteNotificationRepository extends JpaRepository<SiteNotification, Long> {
}