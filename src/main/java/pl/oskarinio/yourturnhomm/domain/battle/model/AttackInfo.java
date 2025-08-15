package pl.oskarinio.yourturnhomm.domain.battle.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AttackInfo {
    @NonNull
    private String UserUUID;
    private BattleUnit atkUnit;
    private BattleUnit defUnit;
    private BattleUnit fasterUnit;
    private BattleUnit slowerUnit;
    private int attackingUnits;
    private int deathUnits;
    private int fasterQuantity;
    private int slowerQuantity;
    private int atkDmg;
    private BattleUnit winnerUnit;
    private BattleUnit loserUnit;
}
