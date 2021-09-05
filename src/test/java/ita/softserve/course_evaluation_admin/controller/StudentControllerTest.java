package ita.softserve.course_evaluation_admin.controller;

import ita.softserve.course_evaluation_admin.configuration.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation_admin.configuration.WithMockCustomUser;
import ita.softserve.course_evaluation_admin.dto.StudentDto;
import ita.softserve.course_evaluation_admin.dto.mapper.StudentDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.service.StudentService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {StudentController.class, SpringSecurityTestConfiguration.class})
class StudentControllerTest {
    private static final String API_STUDENTS_URL = "/api/v1/admin/students";
    private User mike;
    private User joe;
    @MockBean
    private StudentService studentService;

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
        joe = User.builder()
                .id(2L)
                .firstName("Joe")
                .lastName("Smith")
                .email("smith@com")
                .roles(Set.of(Role.ROLE_STUDENT))
                .password("passwordJoe")
                .build();
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void getStudentCandidates() throws Exception {
        List<StudentDto> usersDto = StudentDtoMapper.toDto(List.of(joe, mike));
        Page<StudentDto> userDtoPage = new PageImpl<>(usersDto, PageRequest.of(0, 5), usersDto.size());
        when(studentService.findStudentsNotIncludeGroup(anyInt(), anyString(), any(Pageable.class))).thenReturn(userDtoPage);
        int page = 0;
        int size = 5;
        String filter = "s";
        mvc.perform(get(API_STUDENTS_URL + "/candidates")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("filter", filter)
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(userDtoPage.getTotalElements()))
                .andExpect(status().isOk()).andReturn();
        PageRequest pageRequest = PageRequest
                .of(page, size);
        verify(studentService).findStudentsNotIncludeGroup(Role.ROLE_STUDENT.ordinal(), filter, pageRequest);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void getAllStudentsFromGroups() throws Exception {
        List<StudentDto> usersDto = StudentDtoMapper.toDto(List.of(joe, mike));
        Page<StudentDto> userDtoPage = new PageImpl<>(usersDto, PageRequest.of(0, 5), usersDto.size());
        when(studentService.findAllStudents(any(Pageable.class))).thenReturn(userDtoPage);
        int page = 0;
        int size = 5;
        mvc.perform(get(API_STUDENTS_URL)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(userDtoPage.getTotalElements()))
                .andExpect(status().isOk()).andReturn();
        PageRequest pageRequest = PageRequest
                .of(page, size);
        verify(studentService).findAllStudents(pageRequest);
        verifyNoMoreInteractions(studentService);
    }
}
