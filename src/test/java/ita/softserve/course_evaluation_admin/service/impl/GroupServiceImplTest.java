package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.mapper.CourseDtoMapper;
import ita.softserve.course_evaluation_admin.entity.ChatRoom;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.GroupAlreadyExistException;
import ita.softserve.course_evaluation_admin.exception.exceptions.NotEmptyGroupException;
import ita.softserve.course_evaluation_admin.repository.GroupRepository;
import ita.softserve.course_evaluation_admin.service.ChatRoomService;
import ita.softserve.course_evaluation_admin.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
    private Group if128;
    private Group lv320;
    private Pageable pageable;
    private GroupDto if128Dto;
    private GroupDto lv320Dto;
    private User leon;
    private Course course;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private CourseService courseService;
    @Mock
    private ChatRoomService chatRoomService;
    @InjectMocks
    private GroupServiceImpl groupService;

    @BeforeEach
    void setUp() {
        Set<Course> set = new HashSet<>();
        if128 = Group.builder()
                .id(1L)
                .groupName("IF128")
                .courses(set)
                .students(Collections.emptyList())
                .build();
        if128Dto = GroupDto.builder()
                .id(1L)
                .groupName(if128.getGroupName())
                .students(Collections.emptyList())
                .build();
        lv320 = Group.builder()
                .id(2L)
                .groupName("LV320")
                .students(Collections.emptyList())
                .courses(set)
                .build();
        lv320Dto = GroupDto.builder()
                .id(2L)
                .groupName(lv320.getGroupName())
                .students(Collections.emptyList())
                .build();
        leon = User.builder()
                .firstName("Leon")
                .lastName("Spinks")
                .email("Leon@com")
                .roles(Set.of(Role.ROLE_STUDENT, Role.ROLE_TEACHER))
                .password("password")
                .build();
        pageable = PageRequest.of(0, 55);
        course = Course.builder()
                .id(1L)
                .courseName("TestCourse")
                .description("Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .groups(Set.of(if128))
                .teacher(leon)
                .build();
    }

    @Test
    void findAllGroupDto() {
        List<Group> groups = List.of(if128, lv320);
        Page<Group> groupPage = new PageImpl<>(groups, pageable, groups.size());
        when(groupRepository.findAll(pageable)).thenReturn(groupPage);
        Page<GroupDto> actualPage = groupService.findAllGroupDto(pageable);
        verify(groupRepository).findAll(pageable);
        assertTrue(actualPage.getContent().contains(if128Dto));
        assertTrue(actualPage.getContent().contains(lv320Dto));
    }

    @Test
    void findById() {
        Long id = if128.getId();
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        groupService.findById(id);
        verify(groupRepository).findById(id);
    }

    @Test
    void findByIdExpectException() {
        long id = 3;
        when(groupRepository.findById(id)).thenReturn(Optional.empty());
        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class, () -> groupService.findById(id));
        verify(groupRepository).findById(id);
        assertEquals("The group does not exist by this id: " + id, actual.getMessage());
    }

    @Test
    void deleteById() {
        Long id = if128.getId();
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        groupService.deleteById(id);
        verify(groupRepository).deleteById(id);
    }

    @Test
    void deleteByIdExpectException() {
        Long id = if128.getId();
        if128.setStudents(List.of(leon));
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        NotEmptyGroupException actual = assertThrows(NotEmptyGroupException.class, () -> groupService.deleteById(id));
        verify(groupRepository, never()).deleteById(id);
        assertEquals("The list of students is not empty in the group with id: " + id, actual.getMessage());
    }

    @Test
    void create() {
        String groupName = if128.getGroupName();
        when(groupRepository.findByName(groupName)).thenReturn(Optional.empty());
        when(groupRepository.save(any(Group.class))).thenReturn(any(Group.class));
        groupService.create(groupName);
        verify(chatRoomService).create(any(ChatRoom.class));
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void updateName() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.ofNullable(if128));
        when(groupRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(groupRepository.save(any(Group.class))).thenReturn(any(Group.class));
        groupService.updateName(2, "testName");
        verify(groupRepository).findById(anyLong());
        verify(groupRepository).findByName(anyString());
        verify(groupRepository).save(any(Group.class));
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void updateNameGroupNotFound() {
        Long id = if128.getId();
        when(groupRepository.findById(anyLong())).thenReturn(Optional.empty());
        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class, () -> groupService.updateName(id, "testName"));
        assertEquals("The group does not exist by this id: " + id, actual.getMessage());
        verify(groupRepository).findById(anyLong());
        verify(groupRepository, never()).save(any(Group.class));
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void updateNameAlreadyExist() {
        Long id = lv320.getId();
        String name = if128.getGroupName();
        when(groupRepository.findById(anyLong())).thenReturn(Optional.ofNullable(lv320));
        when(groupRepository.findByName(anyString())).thenReturn(Optional.ofNullable(if128));
        GroupAlreadyExistException actual = assertThrows(GroupAlreadyExistException.class, () -> groupService.updateName(id, name));
        assertEquals("The group already exist by this name: " + name, actual.getMessage());
        verify(groupRepository).findById(anyLong());
        verify(groupRepository).findByName(anyString());
        verify(groupRepository, never()).save(any(Group.class));
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void updateNameWhenNameIsNotChanged() {
        Long id = if128.getId();
        String name = if128.getGroupName();
        when(groupRepository.findById(anyLong())).thenReturn(Optional.ofNullable(if128));
        groupService.updateName(id, name);
        verify(groupRepository).findById(id);
        verify(groupRepository, never()).save(any(Group.class));
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void createExpectException() {
        String groupName = if128.getGroupName();
        when(groupRepository.findByName(groupName)).thenReturn(Optional.ofNullable(if128));
        GroupAlreadyExistException actual = assertThrows(GroupAlreadyExistException.class, () -> groupService.create(groupName));
        verify(groupRepository, never()).save(if128);
        assertEquals("The group already exist by this name: " + groupName, actual.getMessage());
    }

    @Test
    void addCourse() {
        Long id = if128.getId();
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(course.getId())).thenReturn(course);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        CourseDto courseDto = CourseDtoMapper.toDto(course);
        groupService.addCourse(id, courseDto);
        verify(groupRepository).save(if128);
    }

    @Test
    void removeCourse() {
        Long id = if128.getId();
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(course.getId())).thenReturn(course);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        CourseDto courseDto = CourseDtoMapper.toDto(course);
        groupService.removeCourse(id, courseDto);
        verify(groupRepository).save(if128);
    }
}
