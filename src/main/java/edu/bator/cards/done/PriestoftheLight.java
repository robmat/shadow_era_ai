package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class PriestoftheLight extends Ally {

    public PriestoftheLight() {
    }



    public PriestoftheLight(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void wasCasted(GameState gameState) {
        if (gameState.enemyHeroBasedOnPhase().getShadowEnergy() > 0) {
            gameState.enemyHeroBasedOnPhase().setShadowEnergy(gameState.enemyHeroBasedOnPhase().getShadowEnergy() - 1);
        }
    }
}
