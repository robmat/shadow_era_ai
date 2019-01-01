package edu.bator.cards.effects;

import java.util.UUID;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import lombok.Data;

@Data
public abstract class Effect {

    EffectType effectType;
    GamePhase gamePhaseWhenExpires;
    Integer turnEffectExpires;
    String uniqueId = UUID.randomUUID().toString();

    public abstract void applyEffect(Card card);

    public abstract boolean forbidsCounterAttack();

    public enum EffectType {IN_LOVE, ON_FIRE, POISONED, FROZEN, NO_ABILTY}
}
