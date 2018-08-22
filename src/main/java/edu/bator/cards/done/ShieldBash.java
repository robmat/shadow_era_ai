package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.game.GameState;

public class ShieldBash extends Card {

    public ShieldBash(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return card.cardIsAnAlly() && gameState.currentEnemyAlliesBasedOnPhase().contains(card);
    }

    @Override
    public void applyAbility(Card card, GameState gameState) {
        TODO
    }
}