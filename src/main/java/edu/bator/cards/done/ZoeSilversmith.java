package edu.bator.cards.done;

import java.util.function.BiConsumer;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.cards.effects.InLoveEffect;
import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.NoArgsConstructor;

import static java.util.Objects.nonNull;

@NoArgsConstructor
public class ZoeSilversmith extends Ally {

    public ZoeSilversmith(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean hasAbilityToUse(GameState gameState) {
        boolean you = GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
                nonNull(gameState.getYourHero().getWeapon()) &&
                gameState.getYourAllies().contains(this) &&
                gameState.getYourCurrentResources() >= 2;
        boolean enemy = GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
                nonNull(gameState.getEnemyHero().getWeapon()) &&
                gameState.getEnemyAllies().contains(this) &&
                gameState.getEnemyCurrentResources() >= 2;
        boolean abilityReadied = isAbilityReadied();
        return (you || enemy) && abilityReadied;
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return card.cardIsAHero() && nonNull(card.getWeapon()) && card.equals(gameState.yourHeroBasedOnPhase());
    }

    @Override
    public void applyAbility(Card target, GameState gameState) {
        BiConsumer<Card, GameState> abilityFunction = (target1, gameState1) -> {
            Card weapon = target1.getWeapon();
            weapon.setCurrentHp(weapon.getCurrentHp() + 1);
            new GameEngine().decreaseCurrentPlayerResources(gameState, 2);
        };
        target.applyAbility(abilityFunction, gameState);
    }
}