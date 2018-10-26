package edu.bator.cards;

import static java.util.Objects.nonNull;

import edu.bator.game.GameState;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Hero extends Card {

    public Hero(Card cloneFrom) {
        super(cloneFrom);
    }

    public boolean canAttack(GameState gameState) {
        return isAttackReadied() && Objects.nonNull(weapon) && weapon.getCurrentHp() > 0 && weapon.getAttack(gameState) > 0;
    }

    @Override
    public void cardHasDiedEvent(Card card, GameState gameState) {
        Optional.ofNullable(weapon).ifPresent(w -> w.cardHasDiedEvent(card, gameState));
    }

    @Override
    public void attackTarget(GameState gameState, Card target) {
        Card attackSource = this;
        BiConsumer<GameState, Card> attackEvent = (gameState1, card) -> {
            Card weapon = attackSource.getWeapon();
            if (nonNull(weapon) && weapon.getAttack(gameState) > 0) {
                target.setCurrentHp(target.getCurrentHp() - weapon.getAttack(gameState1));
                reduceWeaponHp(gameState1, weapon);
            }
        };
        attackTarget(attackEvent, target, gameState);
    }

    @Override
    public void attackedBy(BiConsumer<GameState, Card> attackEvent, Card source, GameState gameState) {
        int hpBefore = getCurrentHp();
        super.attackedBy(attackEvent, source, gameState);
        if (getCurrentHp() < hpBefore && nonNull(getArmor())) {
            int defence = getArmor().getBaseDefence();
            setCurrentHp(hpBefore - getCurrentHp() > defence ? getCurrentHp() + defence : hpBefore);
            getArmor().setCurrentHp(getArmor().getCurrentHp() - 1);
        }
    }
}
