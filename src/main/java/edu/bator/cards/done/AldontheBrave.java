package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.cards.util.PreventDuplicateCardUtil;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AldontheBrave extends Ally {

  public AldontheBrave(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public void determineCastable(GameState gameState) {
    super.determineCastable(gameState);
    PreventDuplicateCardUtil.preventDuplicates(this, gameState);
  }

  @Override
  public Integer modifiesAttack(Card card, GameState gameState) {
    if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
        card.cardIsAnAlly() &&
        gameState.getYourAllies().contains(card) &&
        gameState.getYourAllies().contains(this)) {
      return 1;
    }
    if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
        card.cardIsAnAlly() &&
        gameState.getEnemyAllies().contains(card) &&
        gameState.getEnemyAllies().contains(this)) {
      return 1;
    }
    return 0;
  }
}