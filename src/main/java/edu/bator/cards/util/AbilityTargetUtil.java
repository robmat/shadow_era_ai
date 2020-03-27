package edu.bator.cards.util;

import edu.bator.cards.Card;
import edu.bator.game.GameState;

public class AbilityTargetUtil {

  public static boolean standardEnemyAllyTargetedAbilityProtectorIncluded(Card target,
      GameState gameState, Card source) {
    boolean possibleAllyTarget = source
        .calculatePossibleEnemyTargetProtectorIncluded(target, gameState);
    return target.cardIsAnAlly() && gameState.currentEnemyAlliesBasedOnPhase().contains(target)
        && possibleAllyTarget;
  }

  public static boolean standardYourAllyTargetedAbilityProtectorIncluded(Card target,
      GameState gameState, Card source) {
    boolean possibleAllyTarget = source
        .calculatePossibleAllyTargetProtectorIncluded(target, gameState);
    return target.cardIsAnAlly() && gameState.currentYourAlliesBasedOnPhase().contains(target)
        && possibleAllyTarget;
  }
}
