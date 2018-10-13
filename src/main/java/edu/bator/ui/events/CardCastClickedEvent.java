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
        gameState.resetPossibleAbiltyTargets();
        gameState.resetPossibleAttackTargets();
        if (gameState.cardIsInHand(card) && GameEngine.ACTION_PHASES
                .contains(gameState.getGamePhase())) {
            if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
                if (card.cardIsSupport()) {
                    handleSupportCast();
                    decreaseEnemyResources();
                } else if (card.cardIsAnAbility()) {
                    handleAbilityCast();
                } else if (card.cardIsArtifact()) {
                    handleArtifactCast();
                    decreaseEnemyResources();
                } else if (gameState.getEnemyHand().remove(card)) {
                    if (card.cardIsAnAlly()) {
                        gameState.getEnemyAllies().add(card);
                    }
                    if (card.cardIsAWeapon()) {
                        gameState.getEnemyHero().setWeapon(card);
                    }

                    decreaseEnemyResources();
                }
                gameState.getEnemyHand().forEach(card -> card.determineCastable(card, gameState));
            }
            if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
                if (card.cardIsSupport()) {
                    handleSupportCast();
                    decreaseYourResources();
                } else if (card.cardIsAnAbility()) {
                    handleAbilityCast();
                } else if (card.cardIsArtifact()) {
                    handleArtifactCast();
                    decreaseYourResources();
                } else if (gameState.getYourHand().remove(card)) {
                    if (card.cardIsAnAlly()) {
                        gameState.getYourAllies().add(card);
                    }
                    if (card.cardIsAWeapon()) {
                        gameState.getYourHero().setWeapon(card);
                    }
                    decreaseYourResources();
                }
                gameState.getYourHand().forEach(card -> card.determineCastable(card, gameState));
            }
            card.wasCasted(gameState);
            gameState.repaint();
            log.info("Casted: " + card);
        }
    }

    private void decreaseYourResources() {
        gameState.setYourCurrentResources(
                gameState.getYourCurrentResources() - card.getResourceCost());
    }

    private void decreaseEnemyResources() {
        gameState.setEnemyCurrentResources(
                gameState.getEnemyCurrentResources() - card.getResourceCost());
    }

    private void handleArtifactCast() {
        new GameEngine().clearAllAbilityAndAttackTargets(gameState);
        card.artifactIsCast(gameState);
    }

    private void handleSupportCast() {
        new GameEngine().clearAllAbilityAndAttackTargets(gameState);
        card.supportIsCast(gameState);
    }

    private void handleAbilityCast() {
        new GameEngine().clearAllAbilityAndAttackTargets(gameState);
        gameState.allCardsInPlay().forEach(target -> target.calculatePossibleAbilityTarget(card, gameState));
        gameState.setAbilitySource(card);
    }
}
