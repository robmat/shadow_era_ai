package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
        boolean possibleAllyTarget = calculatePossibleEnemyTargetProtectorIncluded(card, gameState);
        return card.cardIsAnAlly() && gameState.currentEnemyAlliesBasedOnPhase().contains(card) && possibleAllyTarget;
    }

    @Override
    public void applyAbility(Card target, GameState gameState) {
        applyAbilityDamage(target, gameState, 3);
    }

}