package edu.bator.cards;

import edu.bator.cards.util.BonusUtil;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Weapon extends Card {

    public Weapon(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public Integer getAttack(GameState gameState) {
        return super.getAttack(gameState) + + BonusUtil.attackBonus(gameState, this);
    }
}
