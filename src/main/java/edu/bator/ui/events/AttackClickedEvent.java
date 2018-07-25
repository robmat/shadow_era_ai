package edu.bator.ui.events;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

public class AttackClickedEvent implements EventHandler<MouseEvent> {

  private static final Logger log = Logger.getLogger(AttackClickedEvent.class);

  private final GameState gameState;
  private final Card card;

  public AttackClickedEvent(GameState gameState, Card card) {
    this.gameState = gameState;
    this.card = card;
  }

  @Override
  public void handle(MouseEvent event) {
    if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
      gameState.getEnemyHero().calculatePossibleAttackTarget(card);
      gameState.getEnemyAllies().forEach(target -> target.calculatePossibleAttackTarget(card));

    }
    if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
      gameState.getYourHero().calculatePossibleAttackTarget(card);
      gameState.getYourAllies().forEach(target -> target.calculatePossibleAttackTarget(card));
    }

    gameState.setAttackSource(card);
    gameState.repaint();

    log.info(card + " ready to attack.");
  }
}
