package edu.bator.cards.done;

import java.util.function.BiConsumer;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.cards.effects.NoAbilityEffect;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class DrainPower extends Ability {

    public DrainPower() {
    }

    public DrainPower(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card target, GameState gameState) {
        return gameState.yourAction() && target.equals(gameState.getEnemyHero()) || gameState.enemyAction() && target.equals(gameState.getYourHero());
    }

    @Override
    public void applyAbility(Card target, GameState gameState) {
        BiConsumer<Card, GameState> abilityFunction = (target1, gameState1) -> {
            GamePhase gamePhase = gameState1.getGamePhase();
            int turnWhenExpires = gameState1.getCurrentTurn() + 1;
            if (gamePhase.equals(GamePhase.YOU_ACTION)) {
                gamePhase = GamePhase.YOU_PREPARE;
            }
            if (gamePhase.equals(GamePhase.ENEMY_ACTION)) {
                gamePhase = GamePhase.ENEMY_PREPARE;
            }
            target1.getEffects().add(new NoAbilityEffect(turnWhenExpires, gamePhase));
            target1.setShadowEnergy(target1.getShadowEnergy() - 1 > 0 ? target1.getShadowEnergy() - 1 : 0);
        };
        target.abilityAppliedToMe(abilityFunction, gameState);
        super.applyAbility(target, gameState);
    }
}
