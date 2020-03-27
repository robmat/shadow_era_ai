package edu.bator.cards;

import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Artifact extends Card {

  public Artifact() {
  }

  public Artifact(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public void artifactIsCast(GameState gameState) {
    artifactOrSupportCast(gameState, this);
  }

  static void artifactOrSupportCast(GameState gameState, Card card) {
    if (gameState.getYourHand().remove(card)) {
      gameState.getYourSupport().add(card);
      gameState
          .setYourCurrentResources(gameState.getYourCurrentResources() - card.getResourceCost());

    }
    if (gameState.getEnemyHand().remove(card)) {
      gameState.getEnemySupport().add(card);
      gameState
          .setEnemyCurrentResources(gameState.getEnemyCurrentResources() - card.getResourceCost());
    }
  }
}
