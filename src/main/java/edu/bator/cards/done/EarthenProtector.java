package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.cards.enums.Owner;
import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
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

    @Override
    public void determineCastable(Card card, GameState gameState) {
        super.determineCastable(card, gameState);
        boolean castable = this.isCastable();
        if (GamePhase.YOU_ACTION.equals(gameState.getGamePhase())) {
            boolean anotherAlreadyInPlay = gameState.getYourAllies().stream()
                .anyMatch(c -> c.getName().equals(getName()));
            setCastable(castable && !anotherAlreadyInPlay);
        }
        if (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase())) {
            boolean anotherAlreadyInPlay = gameState.getEnemyAllies().stream()
                .anyMatch(c -> c.getName().equals(getName()));
            setCastable(castable && !anotherAlreadyInPlay);
        }
    }
}
