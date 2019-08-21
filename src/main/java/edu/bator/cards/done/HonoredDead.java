package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.cards.util.HealingUtil;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class HonoredDead extends Ability {

    public HonoredDead() {
    }

    public HonoredDead(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void determineCastable(GameState gameState) {
        super.determineCastable(gameState);
        boolean castable = isCastable();
        boolean you = gameState.yourAction() && gameState.getYourGraveyard().stream().filter(c -> c.cardIsAnAlly()).count() >= 3;
        boolean enemy = gameState.enemyAction() && gameState.getEnemyGraveyard().stream().filter(c -> c.cardIsAnAlly()).count() >= 3;
        setCastable(castable && (you || enemy));
    }

    @Override
    public void wasCasted(GameState gameState) {
        if (gameState.yourAction()) {
            new GameEngine().pickACard(gameState.getYourDeck(), gameState.getYourHand(), gameState.getYourHero());
            new GameEngine().pickACard(gameState.getYourDeck(), gameState.getYourHand(), gameState.getYourHero());
            HealingUtil.heal(2, gameState.getYourHero());
        }
        if (gameState.enemyAction()) {
            new GameEngine().pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand(), gameState.getEnemyHero());
            new GameEngine().pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand(), gameState.getEnemyHero());
            HealingUtil.heal(2, gameState.getEnemyHero());
        }
        new GameEngine().cardDied(this, gameState);
        new GameEngine().decreaseCurrentPlayerResources(gameState, getResourceCost());
    }
}
