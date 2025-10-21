package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WebMvcTest(
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {OncePerRequestFilter.class, Converter.class}))
@AutoConfigureMockMvc(addFilters = false)
public @interface MvcControllerTest {
    Class<?>[] controllers() default {};
}
