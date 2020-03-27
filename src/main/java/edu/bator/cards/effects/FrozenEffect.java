package edu.bator.cards.effects;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FrozenEffect extends Effect {

  public FrozenEffect() {
  }

  public FrozenEffect(Integer turn, GamePhase gamePhase) {
    setEffectType(EffectType.FROZEN);
    setGamePhaseWhenExpires(gamePhase);
    setTurnEffectExpires(turn);
  }

  @Override
  public void applyEffect(Card card) {
    card.setAttackReadied(false);
    card.setAbilityReadied(false);
  }

  @Override
  public boolean forbidsCounterAttack() {
    return true;
  }
}
