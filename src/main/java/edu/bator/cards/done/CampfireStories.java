package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.cards.util.HealingUtil;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import java.util.LinkedList;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class CampfireStories extends Ability {

  public CampfireStories() {
  }

  public CampfireStories(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
    return gameState.yourAction() && gameState.getYourHand().contains(this) ||
        gameState.enemyAction() && gameState.getEnemyHand().contains(this);
  }

  @Override
  public void wasCasted(GameState gameState) {
    if (gameState.yourAction()) {
      new GameEngine()
          .pickACard(gameState.getYourDeck(), gameState.getYourHand(), gameState.getYourHero());
      healAllies(gameState.getYourAllies());
    }
    if (gameState.enemyAction()) {
      new GameEngine()
          .pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand(), gameState.getYourHero());
      healAllies(gameState.getEnemyAllies());
    }
    new GameEngine().clearAllAbilityAndAttackTargets(gameState);
    new GameEngine().decreaseCurrentPlayerResources(gameState, getResourceCost());
    new GameEngine().cardDied(this, gameState);
  }

  private void healAllies(LinkedList<Card> allies) {
    allies.forEach(ally -> {
      HealingUtil.heal(2, ally);
    });
  }
}
