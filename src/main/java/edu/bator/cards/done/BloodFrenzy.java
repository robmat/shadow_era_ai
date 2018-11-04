package edu.bator.cards.done;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class BloodFrenzy extends Attachment {

    public BloodFrenzy() {
    }

    public BloodFrenzy(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        boolean you = gameState.getYourHand().contains(this) && card.equals(gameState.getYourHero());
        boolean enemy = gameState.getEnemyHand().contains(this) && card.equals(gameState.getEnemyHero());
        return super.ableToApplyAbilityTo(card, gameState) && (you || enemy);
    }

    @Override
    public void gamePhaseChangeEvent(GameState gameState) {
        Card yourHero = gameState.getYourHero();
        if (GamePhase.YOU_PREPARE.equals(gameState.getGamePhase()) && yourHero.getAttachments().contains(this)) {
            yourHero.setCurrentHp(yourHero.currentHpWithoutBonus() - 1);
            new GameEngine().pickACard(gameState.getYourDeck(), gameState.getYourHand());
        }
        Card enemyHero = gameState.getEnemyHero();
        if (GamePhase.ENEMY_PREPARE.equals(gameState.getGamePhase()) && enemyHero.getAttachments().contains(this)) {
            enemyHero.setCurrentHp(enemyHero.currentHpWithoutBonus() - 1);
            new GameEngine().pickACard(gameState.getEnemyDeck(), gameState.getEnemyHand());
        }
    }
}
