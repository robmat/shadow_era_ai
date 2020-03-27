package edu.bator.cards.util;

import edu.bator.cards.Card;
import edu.bator.game.GameState;

public class BonusUtil {

  public static Integer attackBonus(GameState gameState, Card target) {
    return gameState.allCardsInPlay()
        .stream()
        .mapToInt(card -> card.modifiesAttack(target, gameState))
        .sum();
  }

  public static Integer hpBonus(GameState gameState, Card target) {
    return gameState.allCardsInPlay()
        .stream()
        .mapToInt(card -> card.modifiesHp(target, gameState))
        .sum();
  }

  ;

}
