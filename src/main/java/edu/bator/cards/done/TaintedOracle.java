package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.cards.enums.Owner;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class TaintedOracle extends Ally {

    public TaintedOracle() {
    }

    public TaintedOracle(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void cardHasDiedEvent(Card card, GameState gameState) {
        if (this.equals(card)) {
            if (this.getOwner().equals(Owner.YOU)) {
                new GameEngine().pickACard(gameState.getYourDeck(), gameState.getYourHand(), gameState.getYourHero());
                new GameEngine().pickACard(gameState.getYourDeck(), gameState.getYourHand(), gameState.getYourHero());
            }
            if (this.getOwner().equals(Owner.ENEMY)) {
                new GameEngine().pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand(), gameState.getEnemyHero());
                new GameEngine().pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand(), gameState.getEnemyHero());
            }
        }
    }
}
