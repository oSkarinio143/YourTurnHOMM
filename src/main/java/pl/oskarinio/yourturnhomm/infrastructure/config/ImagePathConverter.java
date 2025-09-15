package pl.oskarinio.yourturnhomm.infrastructure.config;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImagePathConverter {

    public String convertImageToPath(String name, MultipartFile image) throws IOException {
        String fileName = name + ".png";
        Path path = Paths.get("unit-images", fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return "/" + path.toString();
    }
}
