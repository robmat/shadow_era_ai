package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class RadiantSunlight extends Ability {

  public RadiantSunlight() {
  }

  public RadiantSunlight(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
    boolean you =
        gameState.yourAction() && gameState.getEnemyHero().equals(card) && gameState.getYourHand()
            .contains(this);
    boolean enemy =
        gameState.enemyAction() && gameState.getYourHero().equals(card) && gameState.getEnemyHand()
            .contains(this);
    return card.cardIsAHero() && (you || enemy);
  }

  @Override
  public void applyAbility(Card target, GameState gameState) {
    super.applyAbility(target, gameState);
    target.setShadowEnergy(target.getShadowEnergy() - 2 < 0 ? 0 : target.getShadowEnergy() - 2);
  }
}
