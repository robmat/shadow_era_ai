package edu.bator.ui.events;

import edu.bator.cards.Card;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

public class AttackClickedEvent implements EventHandler<MouseEvent> {

    private static final Logger log = Logger.getLogger(AttackClickedEvent.class);

    private final GameState gameState;
    private final Card card;

    public AttackClickedEvent(GameState gameState, Card card) {
        this.gameState = gameState;
        this.card = card;
    }

    @Override
    public void handle(MouseEvent event) {
        gameState.currentEnemyHeroAndAlliesBasedOnPhase().forEach(target -> {
            target.setPossibleAttackTarget(false);
            target.setPossibleAbilityTarget(false);
            target.calculatePossibleAttackTarget(card, gameState);
        });

        gameState.setAttackSource(card);
        gameState.repaint();

        log.info(card + " ready to attack.");
    }
}
