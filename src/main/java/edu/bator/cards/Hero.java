package edu.bator.cards;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

import edu.bator.cards.enums.CardEnums;
import edu.bator.game.GameState;

import static java.util.Objects.nonNull;

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
            if (nonNull(weapon)) {
                target.setCurrentHp(target.getCurrentHp() - weapon.getAttack(gameState1));
                reduceWeaponHp(gameState1, weapon);
            }
        };
        attackTarget(attackEvent, target, gameState);
    }
}
