package oskarinio143.heroes3;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "unit-images").toString();

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        System.out.println(UPLOAD_DIR);
        System.out.println(UPLOAD_DIR);
        System.out.println(UPLOAD_DIR);
        System.out.println(UPLOAD_DIR);
        registry.addResourceHandler("/unit-images/**")
                .addResourceLocations("file:" + UPLOAD_DIR + "/")
                .setCachePeriod(0);

    }
}
