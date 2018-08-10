package edu.bator.ui.events;


import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

public class SkipSacrificeClickedEvent implements EventHandler<MouseEvent> {

    private static final Logger log = Logger.getLogger(SkipSacrificeClickedEvent.class);

    private GameState gameState;

    public SkipSacrificeClickedEvent(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void handle(MouseEvent event) {
        if (GameEngine.SACRIFICE_PHASES.contains(gameState.getGamePhase())) {
            if (GamePhase.YOU_SACRIFICE.equals(gameState.getGamePhase())) {
                gameState.setGamePhase(GamePhase.YOU_ACTION);
            }
            if (GamePhase.ENEMY_SACRIFICE.equals(gameState.getGamePhase())) {
                gameState.setGamePhase(GamePhase.ENEMY_ACTION);
            }
            new GameEngine().checkGameState(gameState);
            gameState.repaint();
            log.info("Skipped sacrifice.");
        }
    }
}
