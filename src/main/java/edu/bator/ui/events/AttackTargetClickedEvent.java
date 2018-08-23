package edu.bator.ui.events;

import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
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

        attackSource.attackTarget(gameState, attackTarget);

        if (moveToGraveyardIfDead(attackTarget)) { TODO test attacks!
            attackTarget.attackTarget(gameState, attackSource);
        }
        moveToGraveyardIfDead(attackSource);

        log.info(String.format("%s attacked %s", attackTarget, attackSource));
        gameState.resetPossibleAttackTargets();
        attackSource.setAttackReadied(false);
        attackSource.setAbilityReadied(false);
        gameState.setAttackSource(null);
        gameState.repaint();
    }

    private boolean moveToGraveyardIfDead(Card card) {
        if (card.cardIsDead()) {
            new GameEngine().cardDied(card, gameState);
            return true;
        }
        return false;
    }
}
