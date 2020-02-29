package edu.bator.cards.done;

import edu.bator.cards.Artifact;
import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

import static edu.bator.game.GamePhase.ENEMY_PREPARE;
import static edu.bator.game.GamePhase.YOU_PREPARE;

@EqualsAndHashCode(callSuper = true)
public class Bazaar extends Artifact {

    public Bazaar() {
    }

    public Bazaar(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void gamePhaseChangeEvent(GameState gameState) {
        super.gamePhaseChangeEvent(gameState);
        if (gameState.getYourSupport().contains(this) && gameState.getGamePhase().equals(YOU_PREPARE)) {
            GameEngine.pickACard(gameState.getYourDeck(), gameState.getYourHand(), gameState.getYourHero());
        }
        if (gameState.getEnemySupport().contains(this) && gameState.getGamePhase().equals(ENEMY_PREPARE)) {
            GameEngine.pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand(), gameState.getEnemyHero());
        }
    }
}
