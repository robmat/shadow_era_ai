package edu.bator.cards.done;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class CripplingBlow extends Attachment {

  public CripplingBlow() {
  }

  public CripplingBlow(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public Integer modifiesAttack(Card card, GameState gameState) {
    return card.getAttachments().contains(this) && card.cardIsAnAlly() ? Integer.MIN_VALUE : 0;
  }

  @Override
  public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
    boolean possibleAllyTarget = calculatePossibleEnemyTargetProtectorIncluded(card, gameState);
    return super.ableToApplyAbilityTo(card, gameState) &&
        card.cardIsAnAlly() &&
        possibleAllyTarget &&
        gameState.currentEnemyAlliesBasedOnPhase().contains(card);
  }
}
