package roomit.main.domain.jwt.mock;


import org.springframework.security.test.context.support.WithSecurityContext;
import roomit.main.domain.jwt.WithMockCustomMemberSecurityContextFactory;
import roomit.main.domain.member.entity.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomMemberSecurityContextFactory.class)
public @interface WithMockCustomMember {

    String email() default "member@aaa.com";

    Role role() default Role.ROLE_USER;
}
