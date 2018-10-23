package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SmashingBlow extends Ability {

    public SmashingBlow() {
    }

    public SmashingBlow(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void determineCastable(Card card, GameState gameState) {
        super.determineCastable(card, gameState);
    }
}
