package edu.bator.cards;

import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.function.BiConsumer;

import static java.util.Objects.nonNull;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
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

    @Override
    public Integer getAttack(GameState gameState) {
        int bonus = gameState.allCardsInPlay()
                .stream()
                .mapToInt(card -> card.modifiesAttack(this, gameState))
                .sum();
        return super.getAttack(gameState) + bonus;
    }
}
