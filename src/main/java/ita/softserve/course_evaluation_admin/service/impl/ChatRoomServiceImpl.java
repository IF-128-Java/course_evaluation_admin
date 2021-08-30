package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.entity.ChatRoom;
import ita.softserve.course_evaluation_admin.repository.ChatRoomRepository;
import ita.softserve.course_evaluation_admin.service.ChatRoomService;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public ChatRoom create(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }
}