package edu.bator.ui.events;

import static java.util.Objects.nonNull;

import edu.bator.cards.Armor;
import edu.bator.cards.Card;
import edu.bator.cards.Weapon;
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
  @SuppressWarnings("unckecked")
  public void handle(MouseEvent event) {
    gameState.resetPossibleAbilityTargets();
    gameState.resetPossibleAttackTargets();
    if (gameState.cardIsInHand(card) && GameEngine.ACTION_PHASES
        .contains(gameState.getGamePhase())) {
      if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
        if (card.cardIsSupport()) {
          handleSupportCast();
        } else if (card.cardIsAnAbility() && !card.cardIsAttachment()) {
          handleAbilityCast();
        } else if (card.cardIsAttachment()) {
          handleAttachmentCast();
        } else if (card.cardIsArtifact()) {
          handleArtifactCast();
        } else if (gameState.getEnemyHand().remove(card)) {
          if (card.cardIsAnAlly()) {
            gameState.getEnemyAllies().add(card);
          }
          if (card.cardIsAWeapon()) {
            if (nonNull(gameState.getEnemyHero().getWeapon())) {
              gameState.getEnemyGraveyard().add(gameState.getEnemyHero().getWeapon());
            }
            gameState.getEnemyHero().setWeapon((Weapon) card);
          }
          if (card.cardIsAArmor()) {
            if (nonNull(gameState.getEnemyHero().getArmor())) {
              gameState.getEnemyGraveyard().add(gameState.getEnemyHero().getArmor());
            }
            gameState.getEnemyHero().setArmor((Armor) card);
          }
          decreaseEnemyResources();
        }
      }
      if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
        if (card.cardIsSupport()) {
          handleSupportCast();
        } else if (card.cardIsAnAbility() && !card.cardIsAttachment()) {
          handleAbilityCast();
        } else if (card.cardIsAttachment()) {
          handleAttachmentCast();
        } else if (card.cardIsArtifact()) {
          handleArtifactCast();
        } else if (gameState.getYourHand().remove(card)) {
          if (card.cardIsAnAlly()) {
            gameState.getYourAllies().add(card);
          }
          if (card.cardIsAWeapon()) {
            if (nonNull(gameState.getYourHero().getWeapon())) {
              gameState.getYourGraveyard().add(gameState.getYourHero().getWeapon());
            }
            gameState.getYourHero().setWeapon((Weapon) card);
          }
          if (card.cardIsAArmor()) {
            if (nonNull(gameState.getYourHero().getArmor())) {
              gameState.getYourGraveyard().add(gameState.getYourHero().getArmor());
            }
            gameState.getYourHero().setArmor((Armor) card);
          }
          decreaseYourResources();
        }
      }
      card.wasCasted(gameState);
      new GameEngine().determineCurrentHandCardsCastable(gameState);
      gameState.repaint();
      log.info("Casted: " + card);
    }
  }

  private void handleAttachmentCast() {
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
    gameState.allCardsInPlay()
        .forEach(target -> target.calculatePossibleAbilityTarget(card, gameState));
    gameState.setAbilitySource(card);
  }
}
