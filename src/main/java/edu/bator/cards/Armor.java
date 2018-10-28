package edu.bator.cards;

import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Armor extends Card {

    public Armor() {
    }

    public Armor(Card cloneFrom) {
        super(cloneFrom);
    }

    public void armorEffectDuringAttack(Card target, Card source, GameState gameState) {

    }
}