package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

import java.util.function.BiConsumer;

@EqualsAndHashCode(callSuper = true)
public class ArmoredSandworm extends Ally {

    private static final int ARMOR = 2;

    public ArmoredSandworm() {
    }



    public ArmoredSandworm(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void attackedBy(BiConsumer<GameState, Card> attackEvent, Card source, GameState gameState) {
        Integer hpBefore = getCurrentHp(gameState);
        super.attackedBy(attackEvent, source, gameState);
        if (getCurrentHp(gameState) < hpBefore) {
            setCurrentHp(hpBefore - getCurrentHp(gameState) > ARMOR ? getCurrentHp(gameState) + ARMOR : hpBefore);
        }
    }

    @Override
    public void abilityAppliedToMe(BiConsumer<Card, GameState> abilityFunction, GameState gameState) {
        int hpBefore = getCurrentHp(gameState);
        super.abilityAppliedToMe(abilityFunction, gameState);
        if (hpBefore > getCurrentHp(gameState)) {
            setCurrentHp(hpBefore - getCurrentHp(gameState) > ARMOR ? getCurrentHp(gameState) + ARMOR : hpBefore);
        }
    }
}
