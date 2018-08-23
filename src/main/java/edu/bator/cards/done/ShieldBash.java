package edu.bator.cards.done;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;

public class ShieldBash extends Card {

    public ShieldBash(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return card.cardIsAnAlly() && gameState.currentEnemyAlliesBasedOnPhase().contains(card);
    }

    @Override
    public void applyAbility(Card target, GameState gameState) {
        BiConsumer<Card, GameState> abilityFunction = (target1, gameState1) -> {
            if (target1.cardIsAnAlly() && Objects.nonNull(target1.getCurrentHp())) {
                target1.setCurrentHp(target1.getCurrentHp() - 3);
                if (target1.cardIsDead()) {
                    new GameEngine().cardDied(target1, gameState1);
                }
            }
        };
        target.applyAbility(abilityFunction, gameState);
    }
}