package edu.bator.cards;

import java.util.function.BiConsumer;

import edu.bator.game.GameState;

import static java.util.Objects.nonNull;

public class Ally extends Card {
    public Ally(Card cloneFrom) {
        super(cloneFrom);
    }

    public void attackTarget(GameState gameState, Card target) {
        Card attackSource = this;
        BiConsumer<GameState, Card> attackEvent = (gameState1, card) -> {
            if (nonNull(attackSource.getAttack(gameState1)) && nonNull(target.getCurrentHp())) {
                target.setCurrentHp(target.getCurrentHp() - attackSource.getAttack(gameState1));
            }
        };
        attackTarget(attackEvent, target, gameState);
    }
}
