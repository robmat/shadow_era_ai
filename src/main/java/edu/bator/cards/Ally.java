package edu.bator.cards;

import static java.util.Objects.nonNull;

import edu.bator.game.GameState;
import java.util.function.BiConsumer;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
                .mapToInt(card -> card.modifiesAllyAttack(this, gameState))
                .sum();
        return super.getAttack(gameState) + bonus;
    }
}
