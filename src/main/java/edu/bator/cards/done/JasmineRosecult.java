package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;

public class JasmineRosecult extends Card {

  public JasmineRosecult(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public boolean hasAbilityToUse(GameState gameState) {
    boolean you = GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
        gameState.getEnemyAllies().stream().anyMatch(Card::cardIsAnAlly);
    boolean enemy = GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
        gameState.getYourAllies().stream().anyMatch(Card::cardIsAnAlly);
    boolean abilityReadied = isAbilityReadied();
    return (you || enemy) && abilityReadied;
  }

  @Override
  public boolean eligibleForAbility(Card card) {
    return card.cardIsAnAlly();
  }
}