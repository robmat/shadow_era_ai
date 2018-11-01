package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.function.BiConsumer;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShieldBash extends Ability {

    public ShieldBash(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void determineCastable(GameState gameState) {
        super.determineCastable(gameState);
        if (isCastable()) {
            boolean you = gameState.yourAction() && gameState.getEnemyAllies().stream().anyMatch(ally -> ableToApplyAbilityTo(ally, gameState));
            boolean enemy = gameState.yourAction() && gameState.getYourAllies().stream().anyMatch(ally -> ableToApplyAbilityTo(ally, gameState));
            setCastable(you || enemy);
        }
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
        new GameEngine().subtractResources(gameState, 3);
    }
}