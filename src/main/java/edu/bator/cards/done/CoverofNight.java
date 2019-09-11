package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.cards.Expirable;
import edu.bator.cards.Support;
import edu.bator.cards.enums.CardEnums;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"turnExpires", "phaseExpires"})
public class CoverofNight extends Support implements Expirable {

    private int turnExpires;
    private GamePhase phaseExpires;

    public CoverofNight() {
    }

    public CoverofNight(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void wasCasted(GameState gameState) {
        this.turnExpires = gameState.getCurrentTurn() + 2;
        this.phaseExpires = gameState.getGamePhase().getOppositePhase();
    }

    @Override
    public void modifiesAbilities(Set<CardEnums.Ability> abilities, Card card, GameState gameState) {
        if (gameState.getYourSupport().contains(this) && gameState.getYourAllies().contains(card)) {
            abilities.add(CardEnums.Ability.STEALTH);
        }
        if (gameState.getEnemySupport().contains(this) && gameState.getEnemyAllies().contains(card)) {
            abilities.add(CardEnums.Ability.STEALTH);
        }
    }
}
