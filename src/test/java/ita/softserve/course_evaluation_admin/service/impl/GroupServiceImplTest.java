package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.CourseDtoMapper;
import ita.softserve.course_evaluation_admin.dto.mapper.UserDtoMapper;
import ita.softserve.course_evaluation_admin.entity.ChatRoom;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.CourseDateException;
import ita.softserve.course_evaluation_admin.exception.exceptions.GroupAlreadyExistException;
import ita.softserve.course_evaluation_admin.exception.exceptions.NotEmptyGroupException;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import ita.softserve.course_evaluation_admin.repository.GroupRepository;
import ita.softserve.course_evaluation_admin.service.ChatRoomService;
import ita.softserve.course_evaluation_admin.service.CourseService;
import ita.softserve.course_evaluation_admin.service.UserService;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private User mike;
    private User nick;
    private User joe;
    private Course java;
    private Course python;
    private Course web;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private CourseService courseService;
    @Mock
    private UserService userService;
    @Mock
    private ChatRoomService chatRoomService;
    @InjectMocks
    private GroupServiceImpl groupService;


    @BeforeEach
    void setUp() {
        Set<Course> set = new HashSet<>();
        List<User> students = new ArrayList<>();
        List<UserDto> studentsDto = new ArrayList<>();
        if128 = Group.builder()
                .id(1L)
                .groupName("IF128")
                .courses(set)
                .students(students)
                .build();
        if128Dto = GroupDto.builder()
                .id(1L)
                .groupName(if128.getGroupName())
                .students(studentsDto)
                .build();
        lv320 = Group.builder()
                .id(2L)
                .groupName("LV320")
                .students(students)
                .courses(set)
                .build();
        lv320Dto = GroupDto.builder()
                .id(2L)
                .groupName(lv320.getGroupName())
                .students(studentsDto)
                .build();
        leon = User.builder()
                .id(1L)
                .firstName("Leon")
                .lastName("Spinks")
                .email("Leon@com")
                .roles(Set.of(Role.ROLE_STUDENT, Role.ROLE_TEACHER))
                .password("password")
                .build();
        mike = User.builder()
                .id(2L)
                .firstName("Mike")
                .lastName("Tyson")
                .email("mike@com")
                .roles(Set.of(Role.ROLE_TEACHER))
                .password("password")
                .build();
        nick = User.builder()
                .id(3L)
                .firstName("Nick")
                .lastName("Dias")
                .email("dias@com")
                .roles(Set.of(Role.ROLE_STUDENT))
                .password("password")
                .build();
        joe = User.builder()
                .id(4L)
                .firstName("Joe")
                .lastName("Frasier")
                .email("frasier@com")
                .roles(Set.of(Role.ROLE_STUDENT))
                .password("password")
                .build();
        pageable = PageRequest.of(0, 55);
        java = Course.builder()
                .id(1L)
                .courseName("java")
                .description("Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .groups(Set.of(if128))
                .teacher(leon)
                .build();
        python = Course.builder()
                .id(2L)
                .courseName("python")
                .description("Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .groups(Set.of(lv320))
                .teacher(leon)
                .build();
        web = Course.builder()
                .id(3L)
                .courseName("web")
                .description("Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .groups(Set.of(lv320))
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
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void findGroupDtoById() {
        Long id = lv320.getId();
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(lv320));
        GroupDto actual = groupService.findGroupDtoById(id);
        verify(groupRepository).findById(id);
        verifyNoMoreInteractions(groupRepository);
        assertEquals(lv320Dto, actual);
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
        when(courseService.findById(java.getId())).thenReturn(java);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        CourseDto courseDto = CourseDtoMapper.toDto(java);
        groupService.addCourse(id, courseDto);
        verify(groupRepository).save(if128);
    }

    @Test
    void addCompletedCourse() {
        Long id = if128.getId();
        java.setStartDate(LocalDateTime.now().minusMonths(2));
        java.setEndDate(LocalDateTime.now().minusDays(20));
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(java.getId())).thenReturn(java);
        CourseDto courseDto = CourseDtoMapper.toDto(java);
        CourseDateException actual = assertThrows(CourseDateException.class, () -> groupService.addCourse(id, courseDto));
        assertEquals("The selected course with name " + java.getCourseName() + " has already ended!", actual.getMessage());
        verify(groupRepository, never()).save(if128);
    }

    @Test
    void addCompletedCourseEndDateIsNow() {
        Long id = if128.getId();
        java.setStartDate(LocalDateTime.now().minusMonths(2));
        java.setEndDate(LocalDateTime.now());
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(java.getId())).thenReturn(java);
        CourseDto courseDto = CourseDtoMapper.toDto(java);
        CourseDateException actual = assertThrows(CourseDateException.class, () -> groupService.addCourse(id, courseDto));
        assertEquals("The selected course with name " + java.getCourseName() + " has already ended!", actual.getMessage());
        verify(groupRepository, never()).save(if128);
    }

    @Test
    void addActiveCourseStartDateIsInValid() {
        Long id = if128.getId();
        int validDaysAfterStart = 7;
        java.setStartDate(LocalDateTime.now().minusDays(8));
        java.setEndDate(LocalDateTime.now().plusDays(20));
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(java.getId())).thenReturn(java);
        CourseDto courseDto = CourseDtoMapper.toDto(java);
        CourseDateException actual = assertThrows(CourseDateException.class, () -> groupService.addCourse(id, courseDto));
        assertEquals("The selected course with name " + java.getCourseName() + " has already started more then " + validDaysAfterStart + " days ago!", actual.getMessage());
        verify(groupRepository, never()).save(if128);
    }

    @Test
    void addActiveCourseStartDateIsValid() {
        Long id = if128.getId();
        int validDaysAfterStart = 7;
        java.setStartDate(LocalDateTime.now().minusDays(validDaysAfterStart));
        java.setEndDate(LocalDateTime.now().plusDays(20));
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(python.getId())).thenReturn(python);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        CourseDto courseDto = CourseDtoMapper.toDto(python);
        groupService.addCourse(id, courseDto);
        assertTrue(if128.getCourses().contains(python));
        assertFalse(if128.getCourses().contains(web));
        verify(groupRepository).save(if128);
    }

    @Test
    void addExpectedCourse() {
        Long id = if128.getId();
        java.setStartDate(LocalDateTime.now().plusDays(1));
        java.setEndDate(LocalDateTime.now().plusMonths(4));
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(python.getId())).thenReturn(python);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        CourseDto courseDto = CourseDtoMapper.toDto(python);
        groupService.addCourse(id, courseDto);
        assertTrue(if128.getCourses().contains(python));
        assertFalse(if128.getCourses().contains(java));
        assertFalse(if128.getCourses().contains(web));
        verify(groupRepository).save(if128);
    }

    @Test
    void removeCourse() {
        Long id = if128.getId();
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(java.getId())).thenReturn(java);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        CourseDto courseDto = CourseDtoMapper.toDto(java);
        groupService.removeCourse(id, courseDto);
        verify(groupRepository).save(if128);
    }

    @Test
    void removeExpectedCourse() {
        Long id = if128.getId();
        Set<Course> courses = if128.getCourses();
        courses.add(python);
        courses.add(java);
        if128.setCourses(courses);
        java.setStartDate(LocalDateTime.now().plusDays(1));
        java.setEndDate(LocalDateTime.now().plusMonths(4));
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(python.getId())).thenReturn(python);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        CourseDto courseDto = CourseDtoMapper.toDto(python);
        groupService.removeCourse(id, courseDto);
        assertFalse(if128.getCourses().contains(python));
        assertTrue(if128.getCourses().contains(java));
        assertFalse(if128.getCourses().contains(web));
        verify(groupRepository).save(if128);
    }

    @Test
    void removeActiveCourse() {
        Long id = if128.getId();
        Set<Course> courses = if128.getCourses();
        courses.add(python);
        courses.add(java);
        if128.setCourses(courses);
        java.setStartDate(LocalDateTime.now().minusDays(10));
        java.setEndDate(LocalDateTime.now().plusMonths(4));
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(python.getId())).thenReturn(python);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        CourseDto courseDto = CourseDtoMapper.toDto(python);
        groupService.removeCourse(id, courseDto);
        assertFalse(if128.getCourses().contains(python));
        assertTrue(if128.getCourses().contains(java));
        assertFalse(if128.getCourses().contains(web));
        verify(groupRepository).save(if128);
    }

    @Test
    void removeCompletedCourses() {
        Long id = if128.getId();
        Set<Course> courses = if128.getCourses();
        courses.add(python);
        courses.add(java);
        java.setStartDate(LocalDateTime.now().minusDays(20));
        java.setEndDate(LocalDateTime.now().minusDays(5));
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(courseService.findById(java.getId())).thenReturn(java);
        CourseDto courseDto = CourseDtoMapper.toDto(java);
        CourseDateException actual = assertThrows(CourseDateException.class, () -> groupService.removeCourse(id, courseDto));
        assertEquals("The selected course with name " + java.getCourseName() + " has already completed! You cannot delete a completed course!", actual.getMessage());
        verify(groupRepository, never()).save(if128);
    }

    @Test
    void addStudentsToEmptyGroup() {
        Long id = if128.getId();
        Long leonId = leon.getId();
        Long nickId = nick.getId();
        List<User> users = List.of(leon, nick);
        List<UserDto> userDtos = UserDtoMapper.toDto(users);
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(userService.findById(leonId)).thenReturn(leon);
        when(userService.findById(nickId)).thenReturn(nick);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        groupService.addStudents(id, userDtos);
        List<User> actual = if128.getStudents();
        assertEquals(userDtos.size(), actual.size());
        assertTrue(actual.contains(leon));
        assertTrue(actual.contains(nick));
        assertFalse(actual.contains(mike));
        verify(groupRepository).save(if128);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void addStudentsToNotEmptyGroup() {
        Long id = if128.getId();
        Long leonId = leon.getId();
        Long nickId = nick.getId();
        List<User> if128Students = if128.getStudents();
        if128Students.add(joe);
        if128.setStudents(if128Students);
        joe.setGroup(if128);
        List<User> users = List.of(leon, nick);
        List<UserDto> userDtos = UserDtoMapper.toDto(users);
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(userService.findById(leonId)).thenReturn(leon);
        when(userService.findById(nickId)).thenReturn(nick);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        groupService.addStudents(id, userDtos);
        List<User> actual = if128.getStudents();
        assertEquals(userDtos.size() + 1, actual.size());
        assertTrue(actual.contains(leon));
        assertTrue(actual.contains(nick));
        assertTrue(actual.contains(joe));
        assertFalse(actual.contains(mike));
        verify(groupRepository).save(if128);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void addStudentsWhenStudentIncludeInAnotherGroup() {
        Long id = if128.getId();
        Long leonId = leon.getId();
        Long nickId = nick.getId();
        List<User> lv320Students = lv320.getStudents();
        lv320Students.add(leon);
        lv320.setStudents(lv320Students);
        leon.setGroup(lv320);
        List<User> users = List.of(leon, nick);
        List<UserDto> userDtos = UserDtoMapper.toDto(users);
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(userService.findById(leonId)).thenReturn(leon);
        when(userService.findById(nickId)).thenReturn(nick);
        WrongIdException actual = assertThrows(WrongIdException.class, () -> groupService.addStudents(id, userDtos));
        assertEquals("The user with id: " + leonId + " already included in the group with id: " + leon.getGroup().getId(), actual.getMessage());
        verify(groupRepository, never()).save(if128);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void addStudentsGroupNotExist() {
        long id = 2L;
        List<UserDto> emptyList = new ArrayList<>();
        when(groupRepository.findById(anyLong())).thenReturn(Optional.empty());
        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class, () -> groupService.removeStudents(id, emptyList));
        verify(groupRepository).findById(id);
        assertEquals("The group does not exist by this id: " + id, actual.getMessage());
        verify(groupRepository, never()).save(if128);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void addStudentsHasntRoleStudent() {
        Long id = if128.getId();
        Long leonId = leon.getId();
        Long nickId = nick.getId();
        nick.setRoles(Set.of(Role.ROLE_TEACHER));
        List<User> users = List.of(leon, nick);
        List<UserDto> userDtos = UserDtoMapper.toDto(users);
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(userService.findById(leonId)).thenReturn(leon);
        when(userService.findById(nickId)).thenReturn(nick);
        WrongIdException actual = assertThrows(WrongIdException.class, () -> groupService.addStudents(id, userDtos));
        assertEquals("The user with id: " + nickId + " is not a student!", actual.getMessage());
        verify(groupRepository, never()).save(if128);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void removeStudentsAll() {
        Long id = if128.getId();
        Long leonId = leon.getId();
        Long nickId = nick.getId();
        List<User> users = new ArrayList<>();
        users.add(leon);
        users.add(nick);
        leon.setGroup(if128);
        nick.setGroup(if128);
        if128.setStudents(users);
        List<UserDto> userDtos = UserDtoMapper.toDto(users);
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(userService.findById(leonId)).thenReturn(leon);
        when(userService.findById(nickId)).thenReturn(nick);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        groupService.removeStudents(id, userDtos);
        List<User> actual = if128.getStudents();
        assertEquals(0, actual.size());
        verify(groupRepository).save(if128);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void removeStudentsOneStudent() {
        Long id = if128.getId();
        Long nickId = nick.getId();
        List<User> users = new ArrayList<>();
        users.add(leon);
        users.add(nick);
        leon.setGroup(if128);
        nick.setGroup(if128);
        if128.setStudents(users);
        UserDto nickDto = UserDtoMapper.toDto(nick);
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(nickDto);
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(userService.findById(nickId)).thenReturn(nick);
        when(groupRepository.save(any(Group.class))).thenReturn(if128);
        groupService.removeStudents(id, userDtos);
        List<User> actual = if128.getStudents();
        assertEquals(1, actual.size());
        assertTrue(actual.contains(leon));
        assertFalse(actual.contains(nick));
        verify(groupRepository).save(if128);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void removeStudentsNotIncludeGroup() {
        Long id = if128.getId();
        Long nickId = nick.getId();
        List<User> if128users = new ArrayList<>();
        List<User> lv320users = new ArrayList<>();
        if128users.add(leon);
        lv320users.add(nick);
        leon.setGroup(if128);
        nick.setGroup(lv320);
        if128.setStudents(if128users);
        lv320.setStudents(lv320users);
        UserDto nickDto = UserDtoMapper.toDto(nick);
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(nickDto);
        when(groupRepository.findById(id)).thenReturn(Optional.ofNullable(if128));
        when(userService.findById(nickId)).thenReturn(nick);
        WrongIdException actual = assertThrows(WrongIdException.class, () -> groupService.removeStudents(id, userDtos));
        assertEquals("The user with id: " + nickId + " isn't included in the group!", actual.getMessage());
        verify(groupRepository, never()).save(if128);
        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void removeStudentsGroupNotExist() {
        long id = 2L;
        when(groupRepository.findById(anyLong())).thenReturn(Optional.empty());
        List<UserDto> emptyList = new ArrayList<>();
        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class, () -> groupService.removeStudents(id, emptyList));
        verify(groupRepository).findById(id);
        assertEquals("The group does not exist by this id: " + id, actual.getMessage());
        verify(groupRepository, never()).save(if128);
        verifyNoMoreInteractions(groupRepository);
    }
}
