package edu.bator.cards.effects;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NoAbilityEffect extends Effect {

  public NoAbilityEffect() {
  }

  public NoAbilityEffect(Integer turn, GamePhase gamePhase) {
    setEffectType(EffectType.NO_ABILTY);
    setGamePhaseWhenExpires(gamePhase);
    setTurnEffectExpires(turn);
  }

  @Override
  public void applyEffect(Card card) {
    card.setAbilityReadied(false);
  }

  @Override
  public boolean forbidsCounterAttack() {
    return false;
  }
}
