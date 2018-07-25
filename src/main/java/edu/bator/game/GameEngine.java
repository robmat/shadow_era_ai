package edu.bator.game;

import edu.bator.cards.Card;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

public class GameEngine {

  public static final List<GamePhase> ACTION_PHASES = Arrays
      .asList(GamePhase.YOU_ACTION, GamePhase.ENEMY_ACTION);
  public static final List<GamePhase> SACRIFICE_PHASES = Arrays
      .asList(GamePhase.YOU_SACRIFICE, GamePhase.ENEMY_SACRIFICE);
  private static final Logger log = Logger.getLogger(GameEngine.class);

  public void checkGameState(GameState gameState) {
    switch (gameState.getGamePhase()) {
      case YOU_PREPARE: {
        gameState.setCurrentTurn(gameState.getCurrentTurn() + 1);
        pickACard(gameState.getYourDeck(), gameState.getYourHand());
        gameState.setGamePhase(GamePhase.YOU_SACRIFICE);
        readyAllies(gameState.getEnemyAllies());
        readyHero(gameState.getEnemyHero());
        checkGameState(gameState);
        break;
      }
      case YOU_SACRIFICE: {
        break;
      }
      case YOU_ACTION: {
        if (gameState.currentTurn != 1) {
          gameState.increaseSE(gameState.getYourHero());
        }
        gameState.setYourCurrentResources(gameState.getYourResources().size());
        readyHandCards(gameState.getYourHand(), gameState);
        break;
      }

      case ENEMY_PREPARE: {
        pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand());
        gameState.setGamePhase(GamePhase.ENEMY_SACRIFICE);
        if (gameState.currentTurn != 1) {
          readyAllies(gameState.getYourAllies());
        }
        readyHero(gameState.getYourHero());
        checkGameState(gameState);
        break;
      }
      case ENEMY_SACRIFICE: {
        break;
      }
      case ENEMY_ACTION: {
        gameState.setEnemyCurrentResources(gameState.getEnemyResources().size());
        gameState.increaseSE(gameState.getEnemyHero());
        readyHandCards(gameState.getEnemyHand(), gameState);
        break;
      }
    }
    gameState.repaint();
  }

  private void readyHero(Card hero) {
    hero.setAbilityReadied(true);
    hero.setReadied(true);
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
    allies.forEach(Card::tryToReadyAbility);
  }
}
