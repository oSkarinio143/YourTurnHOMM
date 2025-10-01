package pl.oskarinio.yourturnhomm.domain.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class DuelForm {

    private UUID userUUID;
    private Unit leftUnit;
    private Unit rightUnit;
    private Integer leftQuantity;
    private Integer rightQuantity;
    private Integer leftHeroAttack;
    private Integer leftHeroDefense;
    private Integer rightHeroAttack;
    private Integer rightHeroDefense;
}
