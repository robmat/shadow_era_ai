package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.cards.util.AbilityTargetUtil;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Retreat extends Ability {

  public Retreat() {
  }

  public Retreat(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public boolean ableToApplyAbilityTo(Card target, GameState gameState) {
    return
        AbilityTargetUtil.standardEnemyAllyTargetedAbilityProtectorIncluded(target, gameState, this)
            ||
            AbilityTargetUtil
                .standardYourAllyTargetedAbilityProtectorIncluded(target, gameState, this);
  }

  @Override
  public void applyAbility(Card target, GameState gameState) {
    if (AbilityTargetUtil.standardEnemyAllyTargetedAbilityProtectorIncluded(target, gameState, this)
        ||
        AbilityTargetUtil
            .standardYourAllyTargetedAbilityProtectorIncluded(target, gameState, this)) {
      if (gameState.getEnemyAllies().remove(target)) {
        gameState.getEnemyHand().add(target);
      }
      if (gameState.getYourAllies().remove(target)) {
        gameState.getYourHand().add(target);
      }
    }
    super.applyAbility(target, gameState);
  }
}
