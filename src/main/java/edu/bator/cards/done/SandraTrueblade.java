package edu.bator.cards.done;

import java.util.Random;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;

public class SandraTrueblade extends Card {

    public SandraTrueblade(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void wasCasted(GameState gameState) {
        GamePhase currentPhase = gameState.getGamePhase();
        if (GamePhase.YOU_ACTION.equals(currentPhase)) {
            if (gameState.enemyResourcesSize() >= gameState.yourResourcesSize() &&
                    gameState.enemyResourcesSize() > 0) {
                gameState.getEnemyResources().remove(new Random().nextInt(gameState.enemyResourcesSize()));
            }
        }
        if (GamePhase.ENEMY_ACTION.equals(currentPhase)) {
            if (gameState.yourResourcesSize() >= gameState.enemyResourcesSize() &&
                    gameState.yourResourcesSize() > 0) {
                gameState.getYourResources().remove(new Random().nextInt(gameState.enemyResourcesSize()));
            }
        }
    }
}