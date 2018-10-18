package edu.bator.cards.done;

import static java.lang.String.format;

import edu.bator.cards.Card;
import edu.bator.cards.Support;
import edu.bator.cards.enums.Owner;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.log4j.Logger;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValiantDefender extends Support {

    private static final Logger log = Logger.getLogger(ValiantDefender.class);

    private int turnExpires;
    private GamePhase phaseExpires;

    public ValiantDefender() {};
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

    @Override
    public void gamePhaseChangeEvent(GameState gameState) {
        log.debug(format("turn expires: %s, current: %s", turnExpires, gameState.getCurrentTurn()));
        if (Objects.equals(turnExpires, gameState.getCurrentTurn())) {
          log.debug(format("phase expires: %s, current: %s", phaseExpires, gameState.getGamePhase()));
            if (Objects.equals(phaseExpires, gameState.getGamePhase())) {
                if (Objects.equals(getOwner(), Owner.YOU)) {
                    gameState.getYourSupport().remove(this);
                    gameState.getYourGraveyard().add(this);
                }
                if (Objects.equals(getOwner(), Owner.ENEMY)) {
                    gameState.getEnemySupport().remove(this);
                    gameState.getEnemyGraveyard().add(this);
                }
            }
        }
    }
}
