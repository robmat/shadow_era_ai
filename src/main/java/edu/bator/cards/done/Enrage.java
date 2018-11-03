package edu.bator.cards.done;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Enrage extends Attachment {

    public Enrage() {
    }

    public Enrage(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return gameState.yourAction() && card.equals(gameState.getYourHero()) || gameState.enemyAction() && card.equals(gameState.getEnemyHero());
    }

    @Override
    public Integer modifiesHp(Card card, GameState gameState) {
        return card.cardIsAHero() && card.getAttachments().contains(this) ? 10 : 0;
    }
}
