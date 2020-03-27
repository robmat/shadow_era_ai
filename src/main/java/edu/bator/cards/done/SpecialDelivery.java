package edu.bator.cards.done;

import static edu.bator.cards.util.AbilityTargetUtil.standardEnemyAllyTargetedAbilityProtectorIncluded;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SpecialDelivery extends Ability {

  public SpecialDelivery() {
  }

  public SpecialDelivery(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public boolean ableToApplyAbilityTo(Card target, GameState gameState) {
    return standardEnemyAllyTargetedAbilityProtectorIncluded(target, gameState, this);
  }

  @Override
  public void applyAbility(Card target, GameState gameState) {
    super.applyAbility(target, gameState);
    applyAbilityDamage(target, gameState, 3);
  }
}
