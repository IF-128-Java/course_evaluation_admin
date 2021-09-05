package ita.softserve.course_evaluation_admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation_admin.configuration.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation_admin.configuration.WithMockCustomUser;
import ita.softserve.course_evaluation_admin.dto.UserDto;
import ita.softserve.course_evaluation_admin.dto.mapper.UserDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Role;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.service.UserService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserController.class, SpringSecurityTestConfiguration.class})
class UserControllerTest {
    private static final String API_USERS_URL = "/api/v1/admin/users";
    private User mike;
    private User joe;
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private UserService userService;

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
    void getAllASC() throws Exception {
        List<UserDto> usersDto = UserDtoMapper.toDto(List.of(joe, mike));
        Page<UserDto> userDtoPage = new PageImpl<>(usersDto, PageRequest.of(0, 5), usersDto.size());
        when(userService.findAllUserDto(anyString(), any(Integer[].class), any(Pageable.class))).thenReturn(userDtoPage);
        int page = 0;
        int size = 5;
        String search = "s";
        String order = "first_name";
        String direction = "ASC";
        Integer[] roles = new Integer[]{0, 5};
        mvc.perform(get(API_USERS_URL)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("search", search)
                        .param("order", order)
                        .param("direction", direction)
                        .param("roles", Arrays.stream(roles).map(String::valueOf).collect(Collectors.joining(",")))
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(userDtoPage.getTotalElements()))
                .andExpect(status().isOk()).andReturn();
        PageRequest pageRequest = PageRequest
                .of(page, size, Sort.by(order).ascending());
        verify(userService).findAllUserDto(search, roles, pageRequest);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void getAllDESC() throws Exception {
        List<UserDto> usersDto = UserDtoMapper.toDto(List.of(joe, mike));
        Page<UserDto> userDtoPage = new PageImpl<>(usersDto, PageRequest.of(0, 5), usersDto.size());
        when(userService.findAllUserDto(anyString(), any(Integer[].class), any(Pageable.class))).thenReturn(userDtoPage);
        int page = 0;
        int size = 5;
        String search = "s";
        String order = "first_name";
        String direction = "DESC";
        Integer[] roles = new Integer[]{0, 5};
        mvc.perform(get(API_USERS_URL)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("search", search)
                        .param("order", order)
                        .param("direction", direction)
                        .param("roles", Arrays.stream(roles).map(String::valueOf).collect(Collectors.joining(",")))
                )
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").value(userDtoPage.getTotalElements()))
                .andExpect(status().isOk()).andReturn();
        PageRequest pageRequest = PageRequest
                .of(page, size, Sort.by(order).descending());
        verify(userService).findAllUserDto(search, roles, pageRequest);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void getById() throws Exception {
        UserDto mikeDto = UserDtoMapper.toDto(mike);
        Long id = mikeDto.getId();
        when(userService.findUserDtoById(anyLong())).thenReturn(mikeDto);

        mvc.perform(
                        get(API_USERS_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(mikeDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(mikeDto.getLastName()))
                .andExpect(jsonPath("$.roles", hasSize(2)))
                .andExpect(jsonPath("$.roles", hasItem(Role.ROLE_STUDENT.name())))
                .andExpect(jsonPath("$.roles", hasItem(Role.ROLE_TEACHER.name())))
                .andExpect(status().isOk());

        verify(userService).findUserDtoById(anyLong());
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockCustomUser(roles = {Role.ROLE_ADMIN})
    void updateRole() throws Exception {
        UserDto mikeDto = UserDtoMapper.toDto(mike);

        Set<Role> roles = mikeDto.getRoles();
        long id = mikeDto.getId();
        when(userService.updateRoles(id, roles)).thenReturn(mikeDto);

        mvc.perform(patch(API_USERS_URL + "/add-roles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mikeDto)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(mikeDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(mikeDto.getLastName()))
                .andExpect(jsonPath("$.roles", hasSize(2)))
                .andExpect(jsonPath("$.roles", hasItem(Role.ROLE_STUDENT.name())))
                .andExpect(jsonPath("$.roles", hasItem(Role.ROLE_TEACHER.name())))
                .andExpect(status().isOk());

        verify(userService).updateRoles(anyLong(), anySet());
        verifyNoMoreInteractions(userService);
    }
}
