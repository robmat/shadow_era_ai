package edu.bator.cards.done;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.cards.Expirable;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExtraSharp extends Attachment implements Expirable {

    private int turnExpires;
    private GamePhase phaseExpires;

    public ExtraSharp() {
    }

    public ExtraSharp(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        boolean you = gameState.yourAction() && gameState.getYourAllies().contains(card);
        boolean enemy = gameState.enemyAction() && gameState.getEnemyAllies().contains(card);
        return card.cardIsAnAlly() && super.ableToApplyAbilityTo(card, gameState) && (you || enemy);
    }

    @Override
    public void wasCasted(GameState gameState) {
        super.wasCasted(gameState);
        if (gameState.getGamePhase().equals(GamePhase.YOU_ACTION)) {
            phaseExpires = GamePhase.YOU_PREPARE;
            turnExpires = gameState.getCurrentTurn() + 1;
        }
        if (gameState.getGamePhase().equals(GamePhase.ENEMY_ACTION)) {
            phaseExpires = GamePhase.ENEMY_PREPARE;
            turnExpires = gameState.getCurrentTurn() + 1;
        }
    }

    @Override
    public Integer modifiesAttack(Card card, GameState gameState) {
        return card.getAttachments().contains(this) ? 2 : 0;
    }
}
