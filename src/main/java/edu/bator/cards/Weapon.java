package edu.bator.cards;

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
        int bonus = gameState.allCardsInPlay()
                .stream()
                .mapToInt(card -> card.modifiesAttack(this, gameState))
                .sum();
        return super.getAttack(gameState) + bonus;
    }
}
