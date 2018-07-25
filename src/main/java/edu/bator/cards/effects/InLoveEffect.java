package edu.bator.cards.effects;

import edu.bator.game.GamePhase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
public class InLoveEffect extends Effect {

  boolean inLoveEffect = true;

  public InLoveEffect(Integer turn, GamePhase gamePhase) {
    setEffectType(EffectType.IN_LOVE);
    setGamePhaseWhenExpires(gamePhase);
    setTurnEffectExpires(turn);
  }
}
