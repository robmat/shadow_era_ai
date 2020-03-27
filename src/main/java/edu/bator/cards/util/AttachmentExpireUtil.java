package edu.bator.cards.util;

import static java.lang.String.format;

import edu.bator.cards.Card;
import edu.bator.cards.Expirable;
import edu.bator.cards.enums.Owner;
import edu.bator.game.GameState;
import java.util.Objects;
import org.apache.log4j.Logger;

public class AttachmentExpireUtil {

  private static final Logger log = Logger.getLogger(AttachmentExpireUtil.class);

  public static void expireCard(GameState gameState, Expirable expirable) {
    Card card = (Card) expirable;
    log.debug(format("turn expires: %s, current: %s", expirable.getTurnExpires(),
        gameState.getCurrentTurn()));
    if (Objects.equals(expirable.getTurnExpires(), gameState.getCurrentTurn())) {
      log.debug(format("phase expires: %s, current: %s", expirable.getPhaseExpires(),
          gameState.getGamePhase()));
      if (Objects.equals(expirable.getPhaseExpires(), gameState.getGamePhase())) {
        log.debug("Expired: " + expirable);
        if (card.getOwner().equals(Owner.YOU)) {
          gameState.getYourGraveyard().add(card);
        }
        if (card.getOwner().equals(Owner.ENEMY)) {
          gameState.getEnemyGraveyard().add(card);
        }
        //noinspection SuspiciousMethodCalls
        gameState.allCardsInPlay().forEach(c -> c.getAttachments().remove(card));
      }
    }
  }
}
