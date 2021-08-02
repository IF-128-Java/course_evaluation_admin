package ita.softserve.course_evaluation_admin.service.impl;


import ita.softserve.course_evaluation_admin.dto.group.GroupStudentDto;
import ita.softserve.course_evaluation_admin.dto.group.GroupStudentDtoMapper;
import ita.softserve.course_evaluation_admin.dto.group.GroupDto;
import ita.softserve.course_evaluation_admin.dto.group.GroupDtoMapper;
import ita.softserve.course_evaluation_admin.dto.group.GroupStudentRequestDto;
import ita.softserve.course_evaluation_admin.dto.group.GroupStudentRequestDtoMapper;
import ita.softserve.course_evaluation_admin.entity.Group;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import ita.softserve.course_evaluation_admin.repository.GroupRepository;
import ita.softserve.course_evaluation_admin.service.GroupService;
import ita.softserve.course_evaluation_admin.service.UserService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;

    public GroupServiceImpl(GroupRepository groupRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    @Override
    public List<GroupDto> findAll() {
        return GroupDtoMapper.toDto(groupRepository.findAll());
    }

    @Override
    public Group findById(long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new WrongIdException("The group does not exist by this id: " + id));
    }

    @Override
    public GroupStudentRequestDto addStudents(GroupStudentRequestDto dto) {
        Group foundGroup = findById(dto.getId());
        List<User> studentList = dto.getStudentIds()
                .stream()
                .map(userService::findById)
                .peek(u->u.setGroup(foundGroup)).collect(Collectors.toList());
        foundGroup.setStudents(studentList);

        return GroupStudentRequestDtoMapper.toDto(groupRepository.save(foundGroup));
    }

    @Override
    public GroupStudentRequestDto removeStudents(GroupStudentRequestDto dto) {
        Group foundGroup = findById(dto.getId());
        List<User> studentList = dto.getStudentIds()
                .stream()
                .map(userService::findById)
                .peek(u->u.setGroup(null)).collect(Collectors.toList());
        foundGroup.setStudents(studentList);
        return GroupStudentRequestDtoMapper.toDto(groupRepository.save(foundGroup));
    }

    @Override
    public GroupStudentDto getGroupProfile(long id) {
        return GroupStudentDtoMapper.toDto(findById(id));
    }

    @Override
    public Group create(GroupDto dto) {
        dto.setId(null);
        return groupRepository.save(GroupDtoMapper.fromDto(dto));
    }
}
