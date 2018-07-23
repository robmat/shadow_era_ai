package edu.bator.ui.events;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

public class AttackTargetClickedEvent implements EventHandler<MouseEvent> {

    private static final Logger log = Logger.getLogger(AttackTargetClickedEvent.class);

    private final GameState gameState;
    private final Card card;

    public AttackTargetClickedEvent(GameState gameState, Card card) {
        this.gameState = gameState;
        this.card = card;
    }

    @Override
    public void handle(MouseEvent event) {
        Card attackTarget = this.card;
        Card attackSource = gameState.getAttackSource();

        attackTarget.attackedBy(gameState, attackSource);

        moveToGraveyardIfDead(attackTarget);
        moveToGraveyardIfDead(attackSource);

        log.info(String.format("%s attacked %s", attackTarget, attackSource));
        gameState.resetPossibleAttackTargets();
        attackSource.setReadied(false);
        gameState.setAttackSource(null);
        gameState.repaint();
    }

    private void moveToGraveyardIfDead(Card card) {
        if (card.cardIsDead()) {
            if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
                if (gameState.getEnemyAllies().remove(card)) {
                    gameState.getEnemyGraveyard().add(card);
                }
            }
            if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
                if (gameState.getYourAllies().remove(card)) {
                    gameState.getYourGraveyard().add(card);
                }
            }
        }
    }
}
