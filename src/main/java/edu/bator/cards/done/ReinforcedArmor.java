package edu.bator.cards.done;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ReinforcedArmor extends Attachment {

  public ReinforcedArmor() {
  }

  public ReinforcedArmor(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
    boolean you = gameState.getYourHand().contains(this) && gameState.getYourAllies().contains(card)
        && gameState.yourAction();
    boolean enemy =
        gameState.getEnemyHand().contains(this) && gameState.getEnemyAllies().contains(card)
            && gameState.enemyAction();
    return super.ableToApplyAbilityTo(card, gameState) && (you || enemy);
  }

  @Override
  public int armorModifier() {
    return super.armorModifier() + 1;
  }
}
