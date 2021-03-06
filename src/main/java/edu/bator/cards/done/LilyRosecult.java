package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LilyRosecult extends Ally {

  public LilyRosecult(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public void wasCasted(GameState gameState) {
    GamePhase currentPhase = gameState.getGamePhase();
    if (GamePhase.YOU_ACTION.equals(currentPhase) && !gameState.getYourGraveyard().isEmpty()) {
      gameState.getYourHand().add(gameState.getYourGraveyard().removeLast());
    }
    if (GamePhase.ENEMY_ACTION.equals(currentPhase) && !gameState.getEnemyGraveyard().isEmpty()) {
      gameState.getEnemyHand().add(gameState.getEnemyGraveyard().removeLast());
    }
  }
}