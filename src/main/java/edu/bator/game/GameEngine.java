package edu.bator.game;

import edu.bator.cards.Card;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                pickACard(gameState.getYourDeck(), gameState.getYourHand(), gameState.getYourHero());
                expireEffects(gameState);
                applyEffects(gameState.yourHeroAlliesAndSupportCards());
                clearAbilityAndAttackTargets(gameState);
                notifyAllCardsInPlayAboutGamePhase(gameState);

                gameState.setGamePhase(GamePhase.YOU_SACRIFICE);
                checkGameState(gameState);
                break;
            }
            case YOU_SACRIFICE: {
                notifyAllCardsInPlayAboutGamePhase(gameState);
                break;
            }
            case YOU_ACTION: {
                if (gameState.currentTurn != 1) {
                    gameState.increaseSE(gameState.getYourHero());
                }
                gameState.setYourCurrentResources(gameState.yourResourcesSize());
                readyHandCards(gameState.getYourHand(), gameState);
                notifyAllCardsInPlayAboutGamePhase(gameState);
                break;
            }
            case YOU_END: {
                notifyAllCardsInPlayAboutGamePhase(gameState);
                readyAllies(gameState.getEnemyAllies(), gameState);
                readyHero(gameState.getEnemyHero(), gameState);
                gameState.setGamePhase(GamePhase.ENEMY_PREPARE);
                checkGameState(gameState);
                break;
            }
            case ENEMY_PREPARE: {
                pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand(), gameState.getEnemyHero());
                expireEffects(gameState);
                applyEffects(gameState.enemyHeroAlliesAndSupportCards());
                clearAbilityAndAttackTargets(gameState);
                notifyAllCardsInPlayAboutGamePhase(gameState);

                gameState.setGamePhase(GamePhase.ENEMY_SACRIFICE);
                checkGameState(gameState);
                break;
            }
            case ENEMY_SACRIFICE: {
                notifyAllCardsInPlayAboutGamePhase(gameState);
                break;
            }
            case ENEMY_ACTION: {
                gameState.setEnemyCurrentResources(gameState.enemyResourcesSize());
                gameState.increaseSE(gameState.getEnemyHero());
                readyHandCards(gameState.getEnemyHand(), gameState);
                notifyAllCardsInPlayAboutGamePhase(gameState);
                break;
            }
            case ENEMY_END: {
                notifyAllCardsInPlayAboutGamePhase(gameState);
                if (gameState.currentTurn != 1) {
                    readyAllies(gameState.getYourAllies(), gameState);
                }
                readyHero(gameState.getYourHero(), gameState);
                gameState.setGamePhase(GamePhase.YOU_PREPARE);
                checkGameState(gameState);
                break;
            }
        }
        gameState.repaint();
    }

    private void notifyAllCardsInPlayAboutGamePhase(GameState gameState) {
        gameState.allCardsInPlay().forEach(card -> card.gamePhaseChangeEvent(gameState));
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
                .forEach(card -> card.getEffects().forEach(effect -> effect.applyEffect(card)));
    }

    private void expireEffects(GameState gameState) {
        List<Card> cards = Stream.concat(gameState.yourHeroAlliesAndSupportCards().stream(), gameState.enemyHeroAlliesAndSupportCards().stream()).collect(
                Collectors.toList());
        cards.stream()
                .filter(card -> !card.getEffects().isEmpty())
                .forEach(card -> {
                    log.debug(format("Card %s has effects %s", card.toString(), card.getEffects()));
                    log.debug(format("Turn %s phase %s", gameState.getCurrentTurn(), gameState.getGamePhase()));
                    card.getEffects().removeIf(effect ->
                            Objects.equals(effect.getTurnEffectExpires(), gameState.getCurrentTurn()) &&
                                    Objects.equals(effect.getGamePhaseWhenExpires(), gameState.getGamePhase()));
                });
    }

    private void readyHero(Card hero, GameState gameState) {
        hero.tryToReadyAttack(gameState);
        hero.tryToReadyAbility(gameState);
    }

    private void readyHandCards(LinkedList<Card> hand, GameState gameState) {
        hand.forEach(card -> card.determineCastable(gameState));
    }

    public void pickACard(LinkedList<Card> deck, LinkedList<Card> hand, Card hero) {
        if (!deck.isEmpty() && hand.size() < 7) {
            Card card = deck.poll();
            hand.push(card);
            log.info("Picked: " + card);
        } else if (deck.isEmpty()) {
            hero.setCurrentHp(hero.currentHpWithoutBonus() - 1);
        }
    }

    private void readyAllies(LinkedList<Card> allies, GameState gameState) {
        allies.forEach(card -> card.tryToReadyAttack(gameState));
        allies.forEach(card -> card.tryToReadyAbility(gameState));
    }

    public void cardDied(Card card, GameState gameState) {
        cardDied(card, gameState, gameState.getEnemyAllies(), gameState.getEnemyGraveyard());

        cardDied(card, gameState, gameState.getYourAllies(), gameState.getYourGraveyard());

        cardDied(card, gameState, gameState.getEnemyHand(), gameState.getEnemyGraveyard());

        cardDied(card, gameState, gameState.getYourHand(), gameState.getYourGraveyard());

        if (card.equals(gameState.getEnemyHero().getWeapon())) {
            gameState.getEnemyHero().setWeapon(null);
            gameState.getEnemyGraveyard().add(card);
            gameState.allCardsInPlay().forEach(c -> c.cardHasDiedEvent(card, gameState));
            card.cardHasDiedEvent(card, gameState);
        }
        if (card.equals(gameState.getYourHero().getWeapon())) {
            gameState.getYourHero().setWeapon(null);
            gameState.getYourGraveyard().add(card);
            gameState.allCardsInPlay().forEach(c -> c.cardHasDiedEvent(card, gameState));
            card.cardHasDiedEvent(card, gameState);
        }
        if (card.equals(gameState.getEnemyHero().getArmor())) {
            gameState.getEnemyHero().setArmor(null);
            gameState.getEnemyGraveyard().add(card);
            gameState.allCardsInPlay().forEach(c -> c.cardHasDiedEvent(card, gameState));
            card.cardHasDiedEvent(card, gameState);
        }
        if (card.equals(gameState.getYourHero().getArmor())) {
            gameState.getYourHero().setArmor(null);
            gameState.getYourGraveyard().add(card);
            gameState.allCardsInPlay().forEach(c -> c.cardHasDiedEvent(card, gameState));
            card.cardHasDiedEvent(card, gameState);
        }

        if (gameState.yourAction()) {
            readyHandCards(gameState.getYourHand(), gameState);
        }
        if (gameState.enemyAction()) {
            readyHandCards(gameState.getEnemyHand(), gameState);
        }
    }


    private void cardDied(Card card, GameState gameState, LinkedList<Card> diedFrom, GraveyardLinkedList graveyard) {
        if (diedFrom.remove(card)) {
            graveyard.add(card);
            gameState.allCardsInPlay().forEach(c -> c.cardHasDiedEvent(card, gameState));
            card.cardHasDiedEvent(card, gameState);
        }
    }

    public void moveToGraveYard(Card card, GameState gameState) {
        if (gameState.getEnemyHand().remove(card)) {
            gameState.getEnemyGraveyard().add(card);
        }
        if (gameState.getYourHand().remove(card)) {
            gameState.getYourGraveyard().add(card);
        }
    }

    public void clearAllAbilityAndAttackTargets(GameState gameState) {
        gameState.allCardsInPlay().forEach(card -> card.setPossibleAbilityTarget(false));
        gameState.allCardsInPlay().forEach(card -> card.setPossibleAttackTarget(false));
    }

    public void decreaseCurrentPlayerResources(GameState gameState, int resources) {
        if (gameState.yourAction()) {
            gameState.setYourCurrentResources(gameState.getYourCurrentResources() - resources);
        }
        if (gameState.enemyAction()) {
            gameState.setEnemyCurrentResources(gameState.getEnemyCurrentResources() - resources);
        }
    }

    public void determineCurrentHandCardsCastable(GameState gameState) {
        if (gameState.enemyAction()) {
            gameState.getEnemyHand().forEach(card -> card.determineCastable(gameState));
        } else if (gameState.yourAction()) {
            gameState.getYourHand().forEach(card -> card.determineCastable(gameState));
        }
    }
}
