package edu.bator.ui.events;

import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

public class AbilityClickedEvent implements EventHandler<MouseEvent> {

    private static final Logger log = Logger.getLogger(AbilityClickedEvent.class);

    private final GameState gameState;
    private final Card card;

    public AbilityClickedEvent(GameState gameState, Card card) {
        this.gameState = gameState;
        this.card = card;
    }

    @Override
    public void handle(MouseEvent event) {
        if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
            gameState.enemyHeroAlliesAndSupportCards().forEach(target -> target.calculatePossibleAbilityTarget(card));

        }
        if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
            gameState.yourHeroAlliesAndSupportCards().forEach(target -> target.calculatePossibleAbilityTarget(card));
        }

        gameState.setAbilitySource(card);
        gameState.repaint();

        log.info(card + " ready to use ability.");
    }
}
