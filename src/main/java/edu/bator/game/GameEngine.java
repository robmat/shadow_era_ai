package edu.bator.game;

import java.util.LinkedList;

import edu.bator.cards.Card;
import org.apache.log4j.Logger;

public class GameEngine {

    private static final Logger log = Logger.getLogger(GameEngine.class);

    public void checkGameState(GameState gameState) {
        switch (gameState.getGamePhase()) {
            case YOU_PREPARE: {
                readyAllies(gameState.getEnemyAllies());
                pickACard(gameState.getYourDeck(), gameState.getYourHand());
                gameState.setGamePhase(GamePhase.YOU_ACTION);
                checkGameState(gameState);
                break;
            }
            case YOU_ACTION: {
                break;
            }
        }
        gameState.getGamePainter().paint(gameState);
    }

    private void pickACard(LinkedList<Card> deck, LinkedList<Card> hand) {
        if (!deck.isEmpty() && hand.size() < 7) {
            Card card = deck.poll();
            hand.push(card);
            log.info("Picked: " + card);
        }
    }

    private void readyAllies(LinkedList<Card> allies) {
        allies.forEach(ally -> ally.setReadied(true));
    }
}
