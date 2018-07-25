package edu.bator.ui.events;

import edu.bator.cards.Card;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

public class AbilityTargetClickedEvent implements EventHandler<MouseEvent> {

  private static final Logger log = Logger.getLogger(AbilityClickedEvent.class);

  private final GameState gameState;
  private final Card card;

  public AbilityTargetClickedEvent(GameState gameState, Card card) {
    this.gameState = gameState;
    this.card = card;
  }

  @Override
  public void handle(MouseEvent event) {
    gameState.getAbilitySource().applyAbility(this.card, gameState);
    gameState.getAbilitySource().setAbilityReadied(false);
    gameState.setAbilitySource(null);
    gameState.resetPossibleAbiltyTargets();
    gameState.repaint();
  }
}
