package edu.bator.cards.done;

import edu.bator.cards.Artifact;
import edu.bator.cards.Card;
import edu.bator.cards.util.HealingUtil;
import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
public class GoodAscendant extends Artifact {

    public GoodAscendant() {
    }

    public GoodAscendant(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void gamePhaseChangeEvent(GameState gameState) {
        super.gamePhaseChangeEvent(gameState);
        if (GamePhase.YOU_PREPARE.equals(gameState.getGamePhase()) && gameState.getYourSupport().contains(this)) {
            gameState.getYourAllies().forEach(ally -> {
                HealingUtil.heal(2, ally);
            });
        }
        if (GamePhase.ENEMY_PREPARE.equals(gameState.getGamePhase()) && gameState.getEnemySupport().contains(this)) {
            gameState.getEnemyAllies().forEach(ally -> {
                HealingUtil.heal(2, ally);
            });
        }
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        boolean you = gameState.yourAction() &&
                gameState.getYourSupport().contains(this) &&
                gameState.getEnemySupport().stream().anyMatch(c -> Objects.equals(c.getName(), "Evil Ascendant"));

        boolean enemy = gameState.enemyAction() &&
                gameState.getEnemySupport().contains(this) &&
                gameState.getYourSupport().stream().anyMatch(c -> Objects.equals(c.getName(), "Evil Ascendant"));

        return you || enemy;
    }

    @Override
    public void applyAbility(Card target, GameState gameState) { //TODO not tested, no evil ascendant yet
        super.applyAbility(target, gameState);
        new GameEngine().cardDied(target, gameState);
    }
}
