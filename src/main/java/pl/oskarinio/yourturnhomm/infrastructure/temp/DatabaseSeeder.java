package pl.oskarinio.yourturnhomm.infrastructure.temp;

import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.out.repository.UnitRepositoryPort;

public class DatabaseSeeder{

    private final UnitRepositoryPort unitRepositoryPort;

    public DatabaseSeeder(UnitRepositoryPort unitRepositoryPort) {
        this.unitRepositoryPort = unitRepositoryPort;
    }

    public void seedUnits(){
        if(unitRepositoryPort.count() == 0) {
            saveSingleUnit("Pikinier",       4, 5, 1, 3,   10, 4, "odporny na szarżę",                            "https://oskarinio143.github.io/Heroes3assets/images/pikinier.png");
            saveSingleUnit("Halabardnik",    6, 5, 2, 3,   10, 5, "odporny na szarżę",                            "https://oskarinio143.github.io/Heroes3assets/images/halabardnik.png");
            saveSingleUnit("Lucznik",        6, 3,12, 2, 3,   10, 4, "",                                             "https://oskarinio143.github.io/Heroes3assets/images/lucznik.png");
            saveSingleUnit("Kusznik",        6, 5,24, 2, 3,   10, 6, "podwójny strzał",                              "https://oskarinio143.github.io/Heroes3assets/images/kusznik.png");
            saveSingleUnit("Gryf",           8, 8,  3, 6,   25, 6, "dwukrotny kontratak",                         "https://oskarinio143.github.io/Heroes3assets/images/gryf.png");
            saveSingleUnit("Gryf Krolewski", 9, 9,  3, 6,   25, 9, "stały kontratak",                              "https://oskarinio143.github.io/Heroes3assets/images/gryfKrolewski.png");
            saveSingleUnit("Zbrojny",       10,12,  6, 9,   35, 5, "",                                             "https://oskarinio143.github.io/Heroes3assets/images/zbrojny.png");
            saveSingleUnit("Krzyzowiec",    12,12,  7,10,   35, 6, "podwójny atak",                               "https://oskarinio143.github.io/Heroes3assets/images/krzyzowiec.png");
            saveSingleUnit("Mnich",         12, 7,12,10,12,   30, 5, "",                                             "https://oskarinio143.github.io/Heroes3assets/images/mnich.png");
            saveSingleUnit("Kaplan",        12,10,12,10,12,   30, 7, "brak ograniczeń w walce wręcz",               "https://oskarinio143.github.io/Heroes3assets/images/kaplan.png");
            saveSingleUnit("Kawalerzysta",  15,15, 15,25,  100, 7, "szarża",                                       "https://oskarinio143.github.io/Heroes3assets/images/kawalerzysta.png");
            saveSingleUnit("Czempion",      16,16, 20,25,  100, 9, "szarża",                                       "https://oskarinio143.github.io/Heroes3assets/images/czempion.png");
            saveSingleUnit("Aniol",         20,20, 50,50,  200,12, "nienawidzi diabłów",                          "https://oskarinio143.github.io/Heroes3assets/images/aniol.png");
            saveSingleUnit("Archaniol",     30,30, 50,50,  250,18, "+1 do morale; nienawidzi diabłów; wskrzesza poległych sprzymierzeńców", "https://oskarinio143.github.io/Heroes3assets/images/archaniol.png");
        }
    }

    private void saveSingleUnit(String name,
                               int atk,
                               int def,
                               int minDmg,
                               int maxDmg,
                               int hp,
                               int speed,
                               String desc,
                               String imgPath){
        Unit newUnit = new Unit(name, atk, def, minDmg, maxDmg, hp, speed, desc, imgPath);
        unitRepositoryPort.save(newUnit);
    }

    private void saveSingleUnit(String name,
                               int atk,
                               int def,
                               int shots,
                               int minDmg,
                               int maxDmg,
                               int hp,
                               int speed,
                               String desc,
                               String imgPath){
        Unit newUnit = new Unit(name, atk, def, shots, minDmg, maxDmg, hp, speed, desc, imgPath);
        unitRepositoryPort.save(newUnit);
    }
}
