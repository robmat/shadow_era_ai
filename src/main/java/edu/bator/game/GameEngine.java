package edu.bator.game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import edu.bator.cards.Card;
import org.apache.log4j.Logger;

import static java.lang.String.format;

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
                readyAllies(gameState.getEnemyAllies());
                readyHero(gameState.getEnemyHero());
                expireEffects(gameState);
                applyEffects(gameState.yourHeroAlliesAndSupportCards());
                clearAbilityAndAttackTargets(gameState);
                gameState.setGamePhase(GamePhase.YOU_SACRIFICE);
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
                if (gameState.currentTurn != 1) {
                    readyAllies(gameState.getYourAllies());
                }
                readyHero(gameState.getYourHero());
                expireEffects(gameState);
                applyEffects(gameState.enemyHeroAlliesAndSupportCards());
                clearAbilityAndAttackTargets(gameState);
                gameState.setGamePhase(GamePhase.ENEMY_SACRIFICE);
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

    private void clearAbilityAndAttackTargets(GameState gameState) {
        gameState.allCardsInPlay().forEach(card -> {
            card.setPossibleAbilityTarget(false);
            card.setPossibleAttackTarget(false);
        });
    }


    private void applyEffects(LinkedList<Card> cards) {
        cards.stream()
                .filter(card -> !card.getEffects().isEmpty())
                .forEach(card -> {
                    card.getEffects().forEach(effect -> effect.applyEffect(card));
                });
    }

    private void expireEffects(GameState gameState) {
        LinkedList<Card> cards = gameState.getGamePhase().equals(GamePhase.YOU_PREPARE) ?
                gameState.yourHeroAlliesAndSupportCards() : gameState.enemyHeroAlliesAndSupportCards();
        cards.stream()
                .filter(card -> !card.getEffects().isEmpty())
                .forEach(card -> {
                    log.debug(format("Card %s has effects %s", card.getName(), card.getEffects()));
                    log.debug(format("Turn %s pahse %s", gameState.getCurrentTurn(), gameState.getGamePhase()));
                    card.getEffects().removeIf(effect ->
                            Objects.equals(effect.getTurnEffectExpires(), gameState.getCurrentTurn()) &&
                                    Objects.equals(effect.getGamePhaseWhenExpires(), gameState.getGamePhase()));
                });
    }

    private void readyHero(Card hero) {
        hero.setAbilityReadied(true);
        hero.setAttackReadied(true);
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
