package edu.bator.game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.bator.cards.Card;
import org.apache.log4j.Logger;

public class GameEngine {

    private static final Logger log = Logger.getLogger(GameEngine.class);
    public static final List<GamePhase> ACTION_PHASES = Arrays.asList(GamePhase.YOU_ACTION, GamePhase.ENEMY_ACTION);
    public static final List<GamePhase> SACRIFICE_PHASES = Arrays.asList(GamePhase.YOU_SACRIFICE, GamePhase.ENEMY_SACRIFICE);

    public void checkGameState(GameState gameState) {
        switch (gameState.getGamePhase()) {
            case YOU_PREPARE: {
                gameState.setCurrentTurn(gameState.getCurrentTurn() + 1);
                pickACard(gameState.getYourDeck(), gameState.getYourHand());
                gameState.setGamePhase(GamePhase.YOU_SACRIFICE);
                checkGameState(gameState);
                break;
            }
            case YOU_SACRIFICE: {
                break;
            }
            case YOU_ACTION: {
                gameState.setYourCurrentResources(gameState.getYourResources().size());
                readyAllies(gameState.getYourAllies());
                readyHandCards(gameState.getYourHand(), gameState);
                break;
            }

            case ENEMY_PREPARE: {
                pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand());
                gameState.setGamePhase(GamePhase.ENEMY_SACRIFICE);
                checkGameState(gameState);
                break;
            }
            case ENEMY_SACRIFICE: {
                break;
            }
            case ENEMY_ACTION: {
                gameState.setEnemyCurrentResources(gameState.getEnemyResources().size());
                readyAllies(gameState.getEnemyAllies());
                readyHandCards(gameState.getEnemyHand(), gameState);
                break;
            }
        }
        gameState.repaint();
    }

    private void readyHandCards(LinkedList<Card> hand, GameState gameState) {
        hand.forEach(card -> card.determineCastable(card, gameState));
    }

    private void pickACard(LinkedList<Card> deck, LinkedList<Card> hand) {
        if (!deck.isEmpty() && hand.size() < 7) {
            Card card = deck.poll();
            hand.push(card);
            log.info("Picked: " + card);
        }
    }

    private void readyAllies(LinkedList<Card> allies) {
        allies.forEach(Card::tryToReady);
    }
}
