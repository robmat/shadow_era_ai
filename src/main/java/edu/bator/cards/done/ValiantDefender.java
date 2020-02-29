package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.cards.Expirable;
import edu.bator.cards.Support;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.log4j.Logger;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"turnExpires", "phaseExpires"})
public class ValiantDefender extends Support implements Expirable {

    private static final Logger log = Logger.getLogger(ValiantDefender.class);

    private int turnExpires;
    private GamePhase phaseExpires;

    public ValiantDefender() {
    }



    public ValiantDefender(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void supportIsCast(GameState gameState) {
        super.supportIsCast(gameState);
    }

    @Override
    public void wasCasted(GameState gameState) {
        this.turnExpires = gameState.getCurrentTurn() + 2;
        this.phaseExpires = gameState.getGamePhase().getOppositePhase();
    }

    @Override
    protected boolean influenceAttackTargetPossible(Card target, Card attackSource, GameState gameState) {
        if ((gameState.getYourSupport().contains(this) && gameState.getYourAllies().contains(target)) ||
                (gameState.getEnemySupport().contains(this) && gameState.getEnemyAllies().contains(target))) {
            return false;
        }
        return true;
    }
}
