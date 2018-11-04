package edu.bator.cards;

import edu.bator.cards.util.BonusUtil;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.function.BiConsumer;

import static java.util.Objects.nonNull;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ally extends Card {
    public Ally(Card cloneFrom) {
        super(cloneFrom);
    }

    public void attackTarget(GameState gameState, Card target) {
        Card attackSource = this;
        BiConsumer<GameState, Card> attackEvent = (stateOfTheGame, card) -> {
            if (nonNull(attackSource.getAttack(stateOfTheGame)) && nonNull(target.getCurrentHp(gameState))) {
                target.setCurrentHp(target.currentHpWithoutBonus() - attackSource.getAttack(stateOfTheGame));
            }
        };
        attackTarget(attackEvent, target, gameState);
    }

    @Override
    public Integer getAttack(GameState gameState) {
        int attack = super.getAttack(gameState) + BonusUtil.attackBonus(gameState, this);
        return attack < 0 ? 0 : attack;
    }

    @Override
    public Integer getCurrentHp(GameState gameState) {
        return super.getCurrentHp(gameState) + BonusUtil.hpBonus(gameState, this);
    }
}
