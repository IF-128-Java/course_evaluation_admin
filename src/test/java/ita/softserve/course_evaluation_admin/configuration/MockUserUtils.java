package ita.softserve.course_evaluation_admin.configuration;

import ita.softserve.course_evaluation_admin.entity.User;

import java.util.Set;

public class MockUserUtils {
    private MockUserUtils() {
    }

    public static User getUser(WithMockCustomUser customUser) {
        return User.builder()
                .id(customUser.id())
                .roles(Set.of(customUser.roles()))
                .email(customUser.email())
                .password(customUser.password())
                .firstName(customUser.firstName())
                .lastName(customUser.lastName())
                .build();
    }
}
