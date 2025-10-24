package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "testUser", roles = "USER")
public @interface UserProfile {
}
