package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;

public class BorisSkullcrusher extends Card {
    public BorisSkullcrusher(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean hasAbilityToUse(GameState gameState) {
        boolean shadowEnergySufficient = getShadowEnergy() >= 4;
        boolean you = GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
                gameState.getEnemyAllies().stream().anyMatch(ally -> ally.getResourceCost() <= 4 && ally.cardIsAnAlly());
        boolean enemy = GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
                gameState.getYourAllies().stream().anyMatch(ally -> ally.getResourceCost() <= 4 && ally.cardIsAnAlly());

        return shadowEnergySufficient && (you || enemy);
    }

    @Override
    public boolean eligibleForAbility(Card card) {
        return card.cardIsAnAlly() && card.getResourceCost() <= 4;
    }
}