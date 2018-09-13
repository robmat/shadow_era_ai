package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import java.util.Objects;
import java.util.function.BiConsumer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ShieldBash extends Ability {

    public ShieldBash(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        boolean possibleAllyTarget = calculatePossibleTargetProtectorIncluded(card, gameState);
        return card.cardIsAnAlly() && gameState.currentEnemyAlliesBasedOnPhase().contains(card) && possibleAllyTarget;
    }

    @Override
    public void applyAbility(Card target, GameState gameState) {
        BiConsumer<Card, GameState> abilityFunction = (target1, gameState1) -> {
            if (target1.cardIsAnAlly() && Objects.nonNull(target1.getCurrentHp())) {
                target1.setCurrentHp(target1.getCurrentHp() - 3);
            }
        };
        target.abilityAppliedToMe(abilityFunction, gameState);
        if (target.cardIsDead()) {
            new GameEngine().cardDied(target, gameState);
        }
    }
}