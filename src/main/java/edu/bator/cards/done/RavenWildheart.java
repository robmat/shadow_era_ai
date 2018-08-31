package edu.bator.cards.done;

import java.util.function.BiConsumer;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RavenWildheart extends Ally {

    public RavenWildheart(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void attackTarget(BiConsumer<GameState, Card> attackEvent, Card target, GameState gameState) {
        int hpBefore = target.getCurrentHp();
        super.attackTarget(attackEvent, target, gameState);
        if (hpBefore > target.getCurrentHp() && target.cardIsAnAlly()) {
            target.setBaseAttack(0);
        }
    }
}