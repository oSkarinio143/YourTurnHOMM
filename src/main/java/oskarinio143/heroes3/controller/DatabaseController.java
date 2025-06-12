package oskarinio143.heroes3.controller;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import oskarinio143.heroes3.Unit;
import oskarinio143.heroes3.exception.ImageException;
import oskarinio143.heroes3.service.DatabaseService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        databaseService.addUnit(name, attack, defense, shots, minDamage, maxDamage, hp, speed, description, image);
        return "redirect:/oskarinio143/heroes/database";
    }

    @GetMapping("/view")
    public String viewUnits(Model model){
        databaseService.viewUnits(model);
        return "viewUnits";
    }

    @GetMapping("/delete")
    public String deleteUnit(Model model){
        databaseService.viewUnits(model);
        return "deleteUnit";
    }

    @PostMapping("/delete")
    public String handleDeleteUnit(@RequestParam String name) {
        databaseService.removeUnit(name);
        return "redirect:/oskarinio143/heroes/database";
    }

    @GetMapping("/modify")
    public String handleModify(Model model){
        databaseService.viewUnits(model);
        return "modify";
    }

    @GetMapping("/modify/unit")
    public String handleModifyUnit(@RequestParam String name, Model model) {
        Unit unit = databaseService.getUnitByName(name);
        model.addAttribute("unit", unit);
        return "modifyUnit";
    }

    @PostMapping("/modify/unit")
    public String saveModifiedUnit(
            @RequestParam String name,
            @RequestParam int attack,
            @RequestParam int defense,
            @RequestParam int shots,
            @RequestParam int minDamage,
            @RequestParam int maxDamage,
            @RequestParam int hp,
            @RequestParam int speed,
            @RequestParam String description
    ) throws IOException{
        databaseService.modifyUnit(name, attack, defense, shots, minDamage, maxDamage, hp, speed, description);
        return "redirect:/oskarinio143/heroes/database/modify";
    }
}
