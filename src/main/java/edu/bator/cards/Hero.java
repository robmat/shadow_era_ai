package edu.bator.cards;

import java.util.Objects;
import java.util.Optional;

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
}
