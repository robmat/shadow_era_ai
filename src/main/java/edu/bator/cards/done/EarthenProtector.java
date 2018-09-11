package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.cards.enums.Owner;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;

public class EarthenProtector extends Ally {

public EarthenProtector() {};

    public EarthenProtector(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void cardHasDiedEvent(Card card, GameState gameState) {
        if (card.cardIsAnAlly()) {
            if (gameState.getYourAllies().contains(this) && card.getOwner().equals(Owner.YOU)) {
                if (gameState.getYourGraveyard().remove(card)) {
                    gameState.getYourAllies().add(card);
                    card.setBaseAttack(card.getBaseAttack() + 2);
                    card.setCurrentHp(card.getInitialHp() + 2);
                }
                new GameEngine().cardDied(this, gameState);
            }
            if (gameState.getEnemyAllies().contains(this) && card.getOwner().equals(Owner.ENEMY)) {
                if (gameState.getEnemyGraveyard().remove(card)) {
                    gameState.getEnemyAllies().add(card);
                    card.setBaseAttack(card.getBaseAttack() + 2);
                    card.setCurrentHp(card.getInitialHp() + 2);
                }
                new GameEngine().cardDied(this, gameState);
            }
        }
    }
}
