package edu.bator.cards;

import java.util.Objects;
import java.util.Optional;

import edu.bator.game.GameState;

public class Hero extends Card {

    public Hero(Card cloneFrom) {
        super(cloneFrom);
    }

    public boolean canAttack() {
        return isAttackReadied() && Objects.nonNull(weapon) && weapon.getCurrentHp() > 0 && weapon.getAttack() > 0;
    }

    @Override
    public void cardHasDiedEvent(Card card, GameState gameState) {
        Optional.ofNullable(weapon).ifPresent(w -> w.cardHasDiedEvent(card, gameState));
    }
}
