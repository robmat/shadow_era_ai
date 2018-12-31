package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.cards.Expirable;
import edu.bator.cards.Support;
import edu.bator.cards.util.SupportExpireUtil;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class RainDelay extends Support implements Expirable {

    private int turnExpires;
    private GamePhase phaseExpires;


    public RainDelay() {
    }

    public RainDelay(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void supportIsCast(GameState gameState) {
        super.supportIsCast(gameState);
    }

    @Override
    public void wasCasted(GameState gameState) {
        this.turnExpires = gameState.getCurrentTurn() + 1;
        this.phaseExpires = gameState.yourAction() ? GamePhase.YOU_END : GamePhase.ENEMY_END;
        gameState.allCardsInPlay().stream().filter(Card::cardIsAnAlly).forEach(card -> card.setAttackReadied(false));
    }

    @Override
    public boolean preventsAllyFromReadyingAttack(Card attackSource, GameState gameState) {
        return attackSource.cardIsAnAlly();
    }

    @Override
    public void gamePhaseChangeEvent(GameState gameState) {
        SupportExpireUtil.expireCard(gameState, this);
    }

    @Override
    public int getTurnExpires() {
        return turnExpires;
    }

    @Override
    public GamePhase getPhaseExpires() {
        return phaseExpires;
    }
}
