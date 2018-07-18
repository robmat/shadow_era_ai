package edu.bator.ui.events;


import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

public class CardSacrificeClickedEvent implements EventHandler<MouseEvent> {

    private static final Logger log = Logger.getLogger(CardSacrificeClickedEvent.class);

    private Card card;
    private GameState gameState;

    public CardSacrificeClickedEvent(Card card, GameState gameState) {
        this.card = card;
        this.gameState = gameState;
    }

    @Override
    public void handle(MouseEvent event) {
        if (gameState.cardIsInHand(card)) {
            if (GamePhase.YOU_SACRIFICE.equals(gameState.getGamePhase())) {
                gameState.getYourHand().remove(card);
                gameState.getYourResources().add(card);
                log.info("Sacrificed: " + card);
                gameState.setGamePhase(GamePhase.YOU_ACTION);
            }
            if (GamePhase.ENEMY_SACRIFICE.equals(gameState.getGamePhase())) {
                gameState.getEnemyHand().remove(card);
                gameState.getEnemyResources().add(card);
                log.info("Sacrificed: " + card);
                gameState.setGamePhase(GamePhase.ENEMY_ACTION);
            }
            new GameEngine().checkGameState(gameState);
            gameState.repaint();
        }
    }
}
