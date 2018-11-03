package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.function.BiConsumer;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RavenWildheart extends Ally {

    public RavenWildheart(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void attackTarget(BiConsumer<GameState, Card> attackEvent, Card target, GameState gameState) {
        int hpBefore = target.getCurrentHp(gameState);
        super.attackTarget(attackEvent, target, gameState);
        if (hpBefore > target.getCurrentHp(gameState) && target.cardIsAnAlly()) {
            target.setBaseAttack(0);
        }
    }
}