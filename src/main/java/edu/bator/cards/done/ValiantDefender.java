package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.cards.Support;
import edu.bator.cards.enums.Owner;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import java.util.Objects;

public class ValiantDefender extends Support {

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
        if (Objects.equals(turnExpires, gameState.getCurrentTurn())) {
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
