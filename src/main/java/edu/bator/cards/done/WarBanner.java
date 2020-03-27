package edu.bator.cards.done;

import edu.bator.cards.Artifact;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WarBanner extends Artifact {

  public WarBanner(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public Integer modifiesAttack(Card card, GameState gameState) {
    boolean you = gameState.getYourSupport().contains(this) &&
        card.cardIsAnAlly() && gameState.getYourAllies().contains(card);
    boolean enemy = gameState.getEnemySupport().contains(this) &&
        card.cardIsAnAlly() && gameState.getEnemyAllies().contains(card);
    return you || enemy ? 1 : 0;
  }
}
