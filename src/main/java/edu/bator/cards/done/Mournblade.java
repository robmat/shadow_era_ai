package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.cards.Weapon;
import edu.bator.game.GameState;

public class Mournblade extends Weapon {

    public Mournblade(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void cardHasDiedEvent(Card card, GameState gameState) {
        if (card.cardIsAnAlly() && getAttack(gameState) < 5) {
            setAttack(getAttack(gameState) + 1);
        }
    }
}
