package edu.bator.cards.util;

import java.util.Objects;

import edu.bator.cards.Card;
import edu.bator.cards.Expirable;
import edu.bator.game.GameState;
import org.apache.log4j.Logger;

import static java.lang.String.format;

public class SupportExpireUtil {

    private static final Logger log = Logger.getLogger(SupportExpireUtil.class);

    public static void expireCard(GameState gameState, Expirable expirable) {
        log.debug(format("turn expires: %s, current: %s", expirable.getTurnExpires(), gameState.getCurrentTurn()));
        if (Objects.equals(expirable.getTurnExpires(), gameState.getCurrentTurn())) {
            log.debug(format("phase expires: %s, current: %s", expirable.getPhaseExpires(), gameState.getGamePhase()));
            if (Objects.equals(expirable.getPhaseExpires(), gameState.getGamePhase())) {
                log.debug("Expired: " + expirable);
                if (gameState.getYourSupport().remove(expirable)) {
                    gameState.getYourGraveyard().add((Card) expirable);
                }
                if (gameState.getEnemySupport().remove(expirable)) {
                    gameState.getEnemyGraveyard().add((Card) expirable);
                }
            }
        }
    }
}
