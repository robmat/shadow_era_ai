package edu.bator.cards;

import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ability extends Card {

    public Ability(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void applyAbility(Card target, GameState gameState) {
        target.resetFlags();
        super.applyAbility(target, gameState);
    }
}
