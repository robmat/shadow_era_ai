package edu.bator.cards.done;

import java.util.Objects;

import edu.bator.cards.Card;
import edu.bator.game.GameState;

public class ReserveWeapon extends Artifact {

    public ReserveWeapon() {
    }

    public ReserveWeapon(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void supportIsCast(GameState gameState) {
        super.supportIsCast(gameState);
    }

    @Override
    public Integer modifiesAttack(Card card, GameState gameState) {
        if (card.cardIsAWeapon() &&
                card.equals(gameState.getYourHero().getWeapon()) &&
                gameState.getYourSupport().contains(this)
        ) {
            return 1;
        }
        if (card.cardIsAWeapon() &&
                Objects.nonNull(card.getWeapon()) &&
                card.equals(gameState.getEnemyHero().getWeapon()) &&
                gameState.getEnemySupport().contains(this)) {
            return 1;
        }
        return 0;
    }
}
