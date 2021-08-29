package ita.softserve.course_evaluation_admin.repository;

import ita.softserve.course_evaluation_admin.entity.ChatRoom;
import ita.softserve.course_evaluation_admin.entity.ChatType;
import ita.softserve.course_evaluation_admin.entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class GroupRepositoryTest {
    private Group If128;
    private Group LV230;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void beforeEach() {
        ChatRoom chatRoomForIf128 = chatRoomRepository.save(
                ChatRoom.builder()
                        .chatType(ChatType.GROUP)
                        .build()
        );

        ChatRoom chatRoomForLv230 = chatRoomRepository.save(
                ChatRoom.builder()
                        .chatType(ChatType.GROUP)
                        .build()
        );

        If128 = Group.builder()
                .groupName("If128")
                .chatRoom(chatRoomForIf128)
                .build();
        LV230 = Group.builder()
                .groupName("LV230")
                .chatRoom(chatRoomForLv230)
                .build();
    }

    @Test
    void findByNameIf128() {
        Group expected = groupRepository.save(If128);
        Optional<Group> optionalGroup = groupRepository.findByName(If128.getGroupName());
        assertTrue(optionalGroup.isPresent());
        Group actual = optionalGroup.get();
        assertEquals(expected, actual);
    }
    @Test
    void findByNameLV230() {
        Group expected = groupRepository.save(LV230);
        Optional<Group> optionalGroup = groupRepository.findByName(LV230.getGroupName());
        assertTrue(optionalGroup.isPresent());
        Group actual = optionalGroup.get();
        assertEquals(expected, actual);
    }
    @Test
    void findByNameNotExist() {
        Optional<Group> optionalGroup = groupRepository.findByName("notExist");
        assertFalse(optionalGroup.isPresent());
    }
}