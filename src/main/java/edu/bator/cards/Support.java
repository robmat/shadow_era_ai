package edu.bator.cards;

import edu.bator.game.GameState;

public class Support extends Card {
    public Support(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void supportIsCast(GameState gameState) {
        if (gameState.getYourHand().remove(this)) {
            gameState.getYourSupport().add(this);
            gameState.setYourCurrentResources(gameState.getYourCurrentResources() - this.getResourceCost());

        }
        if (gameState.getEnemyHand().remove(this)) {
            gameState.getEnemySupport().add(this);
            gameState.setEnemyCurrentResources(gameState.getEnemyCurrentResources() - this.getResourceCost());
        }
    }
}
