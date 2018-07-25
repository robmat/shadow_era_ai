package edu.bator.cards.effects;

import edu.bator.game.GamePhase;

public class InLoveEffect extends Effect {

  public InLoveEffect(Integer turn, GamePhase gamePhase) {
    setEffectType(EffectType.IN_LOVE);
    setGamePhase(gamePhase);
    setTurnEffectExpires(turn);
  }
}
