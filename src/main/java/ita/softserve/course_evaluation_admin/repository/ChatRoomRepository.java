package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}