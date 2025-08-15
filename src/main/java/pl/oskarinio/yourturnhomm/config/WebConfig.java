package pl.oskarinio.yourturnhomm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.upload.dir}")
    private String uploadDir;

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        Path path = Paths.get(System.getProperty("user.dir"), uploadDir).toAbsolutePath().normalize();

        registry.addResourceHandler("/unit-images/**")
                .addResourceLocations("file:" + path + "/")
                .setCachePeriod(0);
    }
}
