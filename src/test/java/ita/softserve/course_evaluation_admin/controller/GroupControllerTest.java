package ita.softserve.course_evaluation_admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ita.softserve.course_evaluation_admin.configuration.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation_admin.configuration.WithMockCustomUser;
import ita.softserve.course_evaluation_admin.dto.CourseDto;
import ita.softserve.course_evaluation_admin.dto.GroupDto;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.CourseDtoMapper;
import ita.softserve.course_evaluation_admin.dto.mapper.GroupDtoMapper;
import ita.softserve.course_evaluation_admin.dto.mapper.UserDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Course;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.service.CourseService;
import ita.softserve.course_evaluation_admin.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {GroupController.class, SpringSecurityTestConfiguration.class})
class GroupControllerTest {
    private static final String API_URL = "/api/v1/admin/groups";
    private Group if128;
    private Group lv220;
    private Course java;
    private Course web;
    private Course python;
    private User mike;
    private User nick;
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private GroupService groupService;


    @MockBean
    private CourseService courseService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mike = User.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Wood")
                .email("mike@com")
                .roles(Set.of(Role.ROLE_TEACHER, Role.ROLE_STUDENT))
                .password("password")
                .build();
        nick = User.builder()
                .id(2L)
                .firstName("Nick")
                .lastName("Steel")
                .email("steel@com")
                .roles(Set.of( Role.ROLE_STUDENT))
                .password("password")
                .build();
        if128 = Group.builder()
                .id(1L)
                .groupName("If128")
                .build();
        lv220 = Group.builder()
                .id(1L)
                .groupName("LV220")
                .build();
        java = Course.builder()
                .id(1L)
                .courseName("java")
                .description("Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .groups(Set.of(if128))
                .teacher(mike)
                .build();
        web = Course.builder()
                .id(2L)
                .courseName("web")
                .description("Description")
                .startDate(LocalDateTime.now().minusDays(20))
                .endDate(LocalDateTime.now().minusDays(2))
                .groups(Set.of(if128))
                .teacher(mike)
                .build();

        python = Course.builder()
                .id(3L)
                .courseName("python")
                .description("Description")
                .startDate(LocalDateTime.now().plusDays(20))
                .endDate(LocalDateTime.now().plusDays(30))
                .groups(Set.of(lv220))
                .teacher(mike)
                .build();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void getAll() throws Exception {

        int page = 0;
        int size = 5;
        List<GroupDto> groupDtos = GroupDtoMapper.toDto(List.of(if128, lv220));
        Page<GroupDto> groupDtoPage = new PageImpl<>(groupDtos, PageRequest.of(page, size), groupDtos.size());
        when(groupService.findAllGroupDto(any(Pageable.class))).thenReturn(groupDtoPage);

        mvc.perform(get(API_URL)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(groupDtoPage.getTotalElements()))
                .andExpect(status().isOk()).andReturn();

        verify(groupService).findAllGroupDto(any(Pageable.class));
        verifyNoMoreInteractions(groupService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void getById() throws Exception {
        GroupDto javaDto = GroupDtoMapper.toDto(if128);
        when(groupService.findGroupDtoById(anyLong())).thenReturn(javaDto);

        Long id = if128.getId();
        mvc.perform(get(API_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.groupName").value(if128.getGroupName()))
                .andExpect(status().isOk());
        verify(groupService).findGroupDtoById(anyLong());
        verifyNoMoreInteractions(groupService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void getCoursesByGroupIdASC() throws Exception {
        int page = 0;
        int size = 5;
        String order = "group_name";
        String filter = "";
        List<String> status = List.of("ACTUAL", "EXPECTED");
        String direction = "ASC";
        Long id = if128.getId();
        List<CourseDto> courseDtos = CourseDtoMapper.toDto(List.of(java, web));
        PageRequest pageable = PageRequest.of(page, size, Sort.by(order).ascending());
        Page<CourseDto> courseDtoPage = new PageImpl<>(courseDtos, pageable, courseDtos.size());
        when(courseService.findCourseDtoByGroupId(anyLong(), anyString(), any(Pageable.class), anyList())).thenReturn(courseDtoPage);

        mvc.perform(get(API_URL + "/{id}/courses", id)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("filter", filter)
                        .param("order", order)
                        .param("direction", direction)
                        .param("status", String.join(",", status))
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(courseDtoPage.getTotalElements()))
                .andExpect(status().isOk()).andReturn();

        verify(courseService).findCourseDtoByGroupId(id, filter, pageable, status);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void getCoursesByGroupIdDESC() throws Exception {
        int page = 0;
        int size = 5;
        String order = "group_name";
        String filter = "";
        List<String> status = List.of("ACTUAL", "EXPECTED");
        String direction = "DESC";
        Long id = if128.getId();
        List<CourseDto> courseDtos = CourseDtoMapper.toDto(List.of(java, web));
        PageRequest pageable = PageRequest.of(page, size, Sort.by(order).descending());
        Page<CourseDto> courseDtoPage = new PageImpl<>(courseDtos, pageable, courseDtos.size());
        when(courseService.findCourseDtoByGroupId(anyLong(), anyString(), any(Pageable.class), anyList())).thenReturn(courseDtoPage);

        mvc.perform(get(API_URL + "/{id}/courses", id)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("filter", filter)
                        .param("order", order)
                        .param("direction", direction)
                        .param("status", String.join(",", status))
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(courseDtoPage.getTotalElements()))
                .andExpect(status().isOk()).andReturn();

        verify(courseService).findCourseDtoByGroupId(id, filter, pageable, status);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void getAvailableCoursesToAdd() throws Exception {

        int page = 0;
        int size = 5;
        String filter = "";
        Long id = if128.getId();
        List<CourseDto> courseDtos = CourseDtoMapper.toDto(List.of(python));
        PageRequest pageable = PageRequest.of(page, size);
        Page<CourseDto> courseDtoPage = new PageImpl<>(courseDtos, pageable, courseDtos.size());
        when(courseService.findAllByFilterAndExcludeGroup(anyLong(), anyString(), any(Pageable.class))).thenReturn(courseDtoPage);

        mvc.perform(get(API_URL + "/{id}/available-courses", id)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("filter", filter)
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(courseDtoPage.getTotalElements()))
                .andExpect(status().isOk()).andReturn();

        verify(courseService).findAllByFilterAndExcludeGroup(id, filter, pageable);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void createGroup() throws Exception {

        String name = "if128";
        when(groupService.create(name)).thenReturn(if128);

        mvc.perform(post(API_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(name))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(if128.getId()))
                .andExpect(jsonPath("$.groupName").value(if128.getGroupName()))
                .andExpect(status().isCreated());

        verify(groupService).create(anyString());
        verifyNoMoreInteractions(groupService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void updateGroupName() throws Exception {

        String name= "if128";
        Long id = if128.getId();
        when(groupService.updateName(anyLong(),anyString())).thenReturn(if128);

        mvc.perform(patch(API_URL+"/{id}",id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(name))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.groupName").value(if128.getGroupName()))
                .andExpect(status().isCreated());

        verify(groupService).updateName(anyLong(),anyString());
        verifyNoMoreInteractions(groupService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void addStudents() throws Exception {
        List<UserDto> userDtos = List.of(UserDtoMapper.toDto(mike),UserDtoMapper.toDto(nick));
        Long id = if128.getId();
        when(groupService.addStudents(anyLong(),anyList())).thenReturn(if128);

        mvc.perform(patch(API_URL+"/{id}/add-students",id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDtos)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.groupName").value(if128.getGroupName()))
                .andExpect(status().isOk());

        verify(groupService).addStudents(anyLong(),anyList());
        verifyNoMoreInteractions(groupService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void removeStudents() throws Exception {
        List<UserDto> userDtos = List.of(UserDtoMapper.toDto(mike),UserDtoMapper.toDto(nick));
        Long id = if128.getId();
        when(groupService.removeStudents(anyLong(),anyList())).thenReturn(if128);

        mvc.perform(patch(API_URL+"/{id}/remove-students",id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDtos)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.groupName").value(if128.getGroupName()))
                .andExpect(status().isOk());

        verify(groupService).removeStudents(anyLong(),anyList());
        verifyNoMoreInteractions(groupService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void addCourse() throws Exception {
        CourseDto javaDto = CourseDtoMapper.toDto(java);
        Long id = if128.getId();
        when(groupService.addCourse(anyLong(),any(CourseDto.class))).thenReturn(GroupDtoMapper.toDto(if128));

        mvc.perform(patch(API_URL+"/{id}/add-course",id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(javaDto)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.groupName").value(if128.getGroupName()))
                .andExpect(status().isOk());

        verify(groupService).addCourse(anyLong(),any(CourseDto.class));
        verifyNoMoreInteractions(groupService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void removeCourse() throws Exception {
        CourseDto javaDto = CourseDtoMapper.toDto(java);
        Long id = if128.getId();
        when(groupService.removeCourse(anyLong(),any(CourseDto.class))).thenReturn(GroupDtoMapper.toDto(if128));

        mvc.perform(patch(API_URL+"/{id}/remove-course",id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(javaDto)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.groupName").value(if128.getGroupName()))
                .andExpect(status().isOk());

        verify(groupService).removeCourse(anyLong(),any(CourseDto.class));
        verifyNoMoreInteractions(groupService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void deleteGroup() throws Exception {
        Long id = if128.getId();
       doNothing().when(groupService).deleteById(id);
        mvc.perform(delete(API_URL+"/{id}",id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(status().isOk());
        verify(groupService).deleteById(anyLong());
        verifyNoMoreInteractions(groupService);
    }
}