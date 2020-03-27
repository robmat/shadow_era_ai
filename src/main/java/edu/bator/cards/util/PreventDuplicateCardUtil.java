package edu.bator.cards.util;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;

public class PreventDuplicateCardUtil {

  public static void preventDuplicates(Card card, GameState gameState) {
    boolean castable = card.isCastable();
    if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
      boolean anotherAlreadyInPlay = gameState.getYourAllies().stream()
          .anyMatch(c -> c.getName().equals(card.getName()));
      card.setCastable(castable && !anotherAlreadyInPlay);
    }
    if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
      boolean anotherAlreadyInPlay = gameState.getEnemyAllies().stream()
          .anyMatch(c -> c.getName().equals(card.getName()));
      card.setCastable(castable && !anotherAlreadyInPlay);
    }
  }
}
