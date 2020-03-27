package edu.bator.ui.events;

import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TurnSkipClickedEvent implements EventHandler<MouseEvent> {

  private final GameState gameState;

  public TurnSkipClickedEvent(GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public void handle(MouseEvent event) {
    if (GameEngine.ACTION_PHASES.contains(gameState.getGamePhase())) {
      if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
        gameState.setGamePhase(GamePhase.ENEMY_END);
        new GameEngine().checkGameState(gameState);
      }
      if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
        gameState.setGamePhase(GamePhase.YOU_END);
        new GameEngine().checkGameState(gameState);
      }
    }
  }
}
