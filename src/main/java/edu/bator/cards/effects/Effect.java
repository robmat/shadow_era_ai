package edu.bator.cards.effects;

import edu.bator.game.GamePhase;
import lombok.Data;

@Data
public class Effect {

  EffectType effectType;
  GamePhase gamePhaseWhenExpires;
  Integer turnEffectExpires;

  public enum EffectType {IN_LOVE, ON_FIRE, POISONED}
}
