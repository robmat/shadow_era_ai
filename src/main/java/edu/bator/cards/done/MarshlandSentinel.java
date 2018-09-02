package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.cards.util.AbilityTargetUtil;
import edu.bator.game.GameState;

public class MarshlandSentinel extends Ally {

    public MarshlandSentinel() {
    }

    ;

    public MarshlandSentinel(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean hasAbilityToUse(GameState gameState) {
        return gameState.currentYourAlliesBasedOnPhase().contains(this) &&
                gameState.currentEnemyHandBasedOnPhase()
                        .stream()
                        .anyMatch(Card::cardIsAnAlly) &&
                gameState.yourHeroBasedOnPhase().getShadowEnergy() > 0 &&
                isAbilityReadied();
    }

    @Override
    public boolean ableToApplyAbilityTo(Card target, GameState gameState) {
        return new AbilityTargetUtil().standardAllyTargetedAbilityProtectorIncluded(target, gameState, this);
    }

    @Override
    public void applyAbility(Card card, GameState gameState) {
        card.setBaseAttack(0);
        gameState.yourHeroBasedOnPhase().setShadowEnergy(gameState.yourHeroBasedOnPhase().getShadowEnergy() - 1);
    }
}
