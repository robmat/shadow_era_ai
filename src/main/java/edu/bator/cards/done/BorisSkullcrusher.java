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
                gameState.getYourHero().equals(this) &&
                gameState.getEnemyAllies().stream().anyMatch(ally -> ally.getResourceCost() <= 4 && ally.cardIsAnAlly());
        boolean enemy = GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
                gameState.getEnemyHero().equals(this) &&
                gameState.getYourAllies().stream().anyMatch(ally -> ally.getResourceCost() <= 4 && ally.cardIsAnAlly());
        boolean abilityReadied = isAbilityReadied();
        return shadowEnergySufficient && (you || enemy) && abilityReadied;
    }

    @Override
    public boolean eligibleForAbility(Card card) {
        return card.cardIsAnAlly() && card.getResourceCost() <= 4;
    }

    @Override
    public void applyAbility(Card card, GameState gameState) {
        if (card.cardIsAnAlly() && card.getResourceCost() <= 4) {
            if (gameState.getGamePhase().equals(GamePhase.YOU_ACTION)) {
                if (gameState.getEnemyAllies().remove(card)) {
                    gameState.getEnemyGraveyard().add(card);
                }
            }
            if (gameState.getGamePhase().equals(GamePhase.ENEMY_ACTION)) {
                if (gameState.getYourAllies().remove(card)) {
                    gameState.getYourGraveyard().add(card);
                }
            }
            setShadowEnergy(getShadowEnergy() - 4);
        }
    }
}