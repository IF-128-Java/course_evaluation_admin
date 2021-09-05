package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.entity.ChatRoom;
import ita.softserve.course_evaluation_admin.entity.ChatType;
import ita.softserve.course_evaluation_admin.repository.ChatRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @Test
    void create() {
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(any(ChatRoom.class));
        chatRoomService.create(ChatRoom
                .builder()
                .chatType(ChatType.GROUP)
                .build());
        verify(chatRoomRepository).save(any(ChatRoom.class));
        verifyNoMoreInteractions(chatRoomRepository);
    }
}