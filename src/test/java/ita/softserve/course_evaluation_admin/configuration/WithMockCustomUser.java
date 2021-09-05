package ita.softserve.course_evaluation_admin.configuration;

import ita.softserve.course_evaluation_admin.entity.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    long id() default 1;

    String firstName() default "John";

    String lastName() default "Doe";

    String email() default "john@mail.com";

    String password() default "super_password";

    Role[] roles() default {Role.ROLE_STUDENT};
}
