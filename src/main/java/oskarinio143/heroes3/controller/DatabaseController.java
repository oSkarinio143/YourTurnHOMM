package oskarinio143.heroes3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import oskarinio143.heroes3.exception.ImageException;
import oskarinio143.heroes3.service.DatabaseService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/oskarinio143/heroes/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping
    public String choseDatabaseOption(){
        return "choseDatabaseOption";
    }

    @GetMapping("/add")
    public String addUnit(){
        return "addUnit";
    }

    @PostMapping("/add")
    public String handleAddUnit(
            @RequestParam String name,
            @RequestParam int attack,
            @RequestParam int defense,
            @RequestParam int shots,
            @RequestParam int minDamage,
            @RequestParam int maxDamage,
            @RequestParam int hp,
            @RequestParam int speed,
            @RequestParam String description,
            @RequestParam MultipartFile image
    ) throws IOException{
        String fileName = name + ".png";
        Path path = Paths.get("unit-images", fileName);
        Files.createDirectories(path.getParent()); // jeśli folder nie istnieje
        Files.write(path, image.getBytes());
        String imagePath = path.toString();

        databaseService.saveUnit(name, attack, defense, shots, minDamage, maxDamage, hp, speed, description, imagePath);
        return "redirect:/oskarinio143/heroes/database";
    }
}
