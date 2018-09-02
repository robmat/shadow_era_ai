package edu.bator.cards.util;

import edu.bator.cards.Card;
import edu.bator.game.GameState;

public class AbilityTargetUtil {

    public boolean standardAllyTargetedAbilityProtectorIncluded(Card target, GameState gameState, Card source) {
        boolean possibleAllyTarget = source.calculatePossibleTargetProtectorIncluded(target, gameState);
        return target.cardIsAnAlly() && gameState.currentEnemyAlliesBasedOnPhase().contains(target) && possibleAllyTarget;
    }
}
