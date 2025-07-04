package oskarinio143.heroes3.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import oskarinio143.heroes3.model.Route;
import oskarinio143.heroes3.service.DatabaseService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping(Route.MAIN + Route.DATABASE)
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping
    public String choseDatabaseOption(){
        return "choseDatabaseOption";
    }

    @GetMapping(Route.ADD)
    public String addUnit(){
        return "addUnit";
    }

    @PostMapping("/add")
    public String handleAddUnit(@Valid
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
        return Route.REDIRECT + Route.DATABASE;
    }

    @GetMapping(Route.VIEW)
    public String viewUnits(Model model){
        databaseService.viewUnits(model);
        return "viewUnits";
    }

    @GetMapping(Route.DELETE)
    public String deleteUnit(Model model){
        databaseService.viewUnits(model);
        return "deleteUnit";
    }

    @PostMapping(Route.DELETE)
    public String handleDeleteUnit(@RequestParam String name) {
        databaseService.removeUnit(name);
        return Route.REDIRECT + Route.DATABASE;
    }

    @GetMapping(Route.MODIFY)
    public String handleModify(Model model){
        databaseService.viewUnits(model);
        return "modify";
    }

    @GetMapping(Route.MODIFY + Route.UNIT)
    public String handleModifyUnit(@RequestParam String name, Model model) {
        databaseService.viewSingleUnit(model, name);
        return "modifyUnit";
    }

    @PostMapping(Route.MODIFY + Route.UNIT)
    public String saveModifiedUnit(@Valid
            @RequestParam String name,
            @RequestParam int attack,
            @RequestParam int defense,
            @RequestParam int shots,
            @RequestParam int minDamage,
            @RequestParam int maxDamage,
            @RequestParam int hp,
            @RequestParam int speed,
            @RequestParam Optional<String> description
    ){
        databaseService.modifyUnit(name, attack, defense, shots, minDamage, maxDamage, hp, speed, description);
        return Route.REDIRECT + Route.DATABASE + Route.MODIFY;

    }
}
