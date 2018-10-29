package edu.bator.cards;

import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Artifact extends Card {

    public Artifact() {
    }

    public Artifact(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void artifactIsCast(GameState gameState) {
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
