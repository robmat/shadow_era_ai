package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.cards.Expirable;
import edu.bator.cards.Support;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;

public class UrgentBusiness extends Support implements Expirable {

    private int turnExpires;
    private GamePhase phaseExpires;

    public UrgentBusiness() {
    }

    public UrgentBusiness(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean preventsAllyOrHeroFromReadyingAttack(Card card, GameState gameState) {
        return card.cardIsAHero();
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
