package edu.bator.cards.effects;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InLoveEffect extends Effect {

  public InLoveEffect() {
  }

  public InLoveEffect(Integer turn, GamePhase gamePhase) {
    setEffectType(EffectType.IN_LOVE);
    setGamePhaseWhenExpires(gamePhase);
    setTurnEffectExpires(turn);
  }

  @Override
  public void applyEffect(Card card) {
    card.setAttackReadied(false);
  }

  @Override
  public boolean forbidsCounterAttack() {
    return false;
  }
}
