package edu.bator.cards.done;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import org.apache.log4j.Logger;

import static java.lang.String.format;

@EqualsAndHashCode(callSuper = true)
public class Enrage extends Attachment {

    private static final Logger log = Logger.getLogger(Enrage.class);

    public Enrage() {
    }

    public Enrage(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return gameState.yourAction() &&
                super.ableToApplyAbilityTo(card, gameState) &&
                card.equals(gameState.getYourHero()) ||
                gameState.enemyAction() && card.equals(gameState.getEnemyHero());
    }

    @Override
    public Integer modifiesHp(Card card, GameState gameState) {
        int bonus = card.cardIsAHero() && card.getAttachments().contains(this) ? 10 : 0;
        log.debug(format("card %s, attachments %s, bonus %s", card, card.getAttachments(), bonus));
        return bonus;
    }
}
