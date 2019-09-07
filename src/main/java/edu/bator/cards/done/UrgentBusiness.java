package edu.bator.cards.done;

import java.util.Objects;

import edu.bator.cards.Card;
import edu.bator.cards.Expirable;
import edu.bator.cards.Support;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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
        return card.cardIsAHero() && gameState.allCardsInPlay().stream().anyMatch(c -> Objects.equals(this, c));
    }

    @Override
    public void wasCasted(GameState gameState) {
        super.wasCasted(gameState);
        gameState.getYourHero().setAttackReadied(false);
        gameState.getEnemyHero().setAttackReadied(false);
        if (gameState.getGamePhase().equals(GamePhase.YOU_ACTION)) {
            phaseExpires = GamePhase.YOU_END;
            turnExpires = gameState.getCurrentTurn() + 1;
        }
        if (gameState.getGamePhase().equals(GamePhase.ENEMY_ACTION)) {
            phaseExpires = GamePhase.ENEMY_END;
            turnExpires = gameState.getCurrentTurn() + 1;
        }
    }
}