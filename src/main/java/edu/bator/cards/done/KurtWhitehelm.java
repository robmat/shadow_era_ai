package edu.bator.cards.done;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KurtWhitehelm extends Ally {

    public KurtWhitehelm(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public Integer getAttack(GameState gameState) {
        boolean inEnemyAllies = gameState.getEnemyAllies().contains(this);
        boolean inYourAllies = gameState.getYourAllies().contains(this);
        List<Card> allies = new ArrayList<>();

        if (inEnemyAllies) allies = gameState.getEnemyAllies();
        if (inYourAllies) allies = gameState.getYourAllies();

        boolean twoOrMoreAlliesInPlay = allies
                .stream()
                .filter(card -> !card.equals(this))
                .filter(Card::cardIsAnAlly)
                .count() >= 2;
        return super.getAttack(gameState) + (twoOrMoreAlliesInPlay ? 1 : 0);
    }

    @Override
    public void applyAbility(BiConsumer<Card, GameState> abilityFunction, GameState gameState) {
        int hpBefore = getCurrentHp();
        super.applyAbility(abilityFunction, gameState);
        if (hpBefore > getCurrentHp()) {
            setCurrentHp(getCurrentHp() + 1);
        }
    }
}