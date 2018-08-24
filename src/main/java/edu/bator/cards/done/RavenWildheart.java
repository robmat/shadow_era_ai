package edu.bator.cards.done;

import java.util.function.BiConsumer;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.game.GameState;

public class RavenWildheart extends Ally {

    public RavenWildheart(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void attackTarget(BiConsumer<GameState, Card> attackEvent, Card target, GameState gameState) {
        int hpBefore = target.getCurrentHp();
        attackEvent.accept(gameState, target);
        if (hpBefore > target.getCurrentHp()) {
            target.setAttack(0);
        }
    }
}