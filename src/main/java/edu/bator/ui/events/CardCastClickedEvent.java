package edu.bator.ui.events;

import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

public class CardCastClickedEvent implements EventHandler<MouseEvent> {

    private static final Logger log = Logger.getLogger(CardCastClickedEvent.class);

    private final GameState gameState;
    private final Card card;

    public CardCastClickedEvent(GameState gameState, Card card) {
        this.gameState = gameState;
        this.card = card;
    }

    @Override
    public void handle(MouseEvent event) {
        if (gameState.cardIsInHand(card) && GameEngine.ACTION_PHASES
                .contains(gameState.getGamePhase())) {
            if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
                if (gameState.getEnemyHand().remove(card)) {
                    if (card.cardIsAnAlly()) {
                        gameState.getEnemyAllies().add(card);
                    }
                    if (card.cardIsAWeapon()) {
                        gameState.getEnemyHero().setWeapon(card);
                    }
                    gameState.setEnemyCurrentResources(
                            gameState.getEnemyCurrentResources() - card.getResourceCost());
                    gameState.getEnemyHand().forEach(card -> card.determineCastable(card, gameState));
                }
            }
            if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
                if (gameState.getYourHand().remove(card)) {
                    if (card.cardIsAnAlly()) {
                        gameState.getYourAllies().add(card);
                    }
                    if (card.cardIsAWeapon()) {
                        gameState.getYourHero().setWeapon(card);
                    }
                    gameState.setYourCurrentResources(
                            gameState.getYourCurrentResources() - card.getResourceCost());
                    gameState.getYourHand().forEach(card -> card.determineCastable(card, gameState));
                }
            }
            card.wasCasted(gameState);
            gameState.repaint();
            log.info("Casted: " + card);
        }
    }
}
