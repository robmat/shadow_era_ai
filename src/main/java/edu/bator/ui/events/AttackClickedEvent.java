package edu.bator.ui.events;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AttackClickedEvent implements EventHandler<MouseEvent> {

    private final GameState gameState;
    private final Card card;

    public AttackClickedEvent(GameState gameState, Card card) {
        this.gameState = gameState;
        this.card = card;
    }

    @Override
    public void handle(MouseEvent event) {
        if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
            gameState.getEnemyHero().calculatePossibleAttackTarget(card);
            gameState.getEnemyAllies().forEach(target -> target.calculatePossibleAttackTarget(card));
            gameState.repaint();
        }
        if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
            gameState.getYourHero().calculatePossibleAttackTarget(card);
            gameState.getYourAllies().forEach(target -> target.calculatePossibleAttackTarget(card));
            gameState.repaint();
        }
    }
}
