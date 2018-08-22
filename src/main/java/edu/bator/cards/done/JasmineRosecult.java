package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.cards.effects.InLoveEffect;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;

public class JasmineRosecult extends Ally {

    public JasmineRosecult(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean hasAbilityToUse(GameState gameState) {
        boolean you = GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
                gameState.yourHeroAlliesAndSupportCards().contains(this) &&
                gameState.getEnemyAllies().stream().anyMatch(Card::cardIsAnAlly) &&
                gameState.getYourCurrentResources() >= 2;
        boolean enemy = GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
                gameState.enemyHeroAlliesAndSupportCards().contains(this) &&
                gameState.getYourAllies().stream().anyMatch(Card::cardIsAnAlly) &&
                gameState.getEnemyCurrentResources() >= 2;
        boolean abilityReadied = isAbilityReadied();
        return (you || enemy) && abilityReadied;
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return card.cardIsAnAlly();
    }

    @Override
    public void applyAbility(Card card, GameState gameState) {
        GamePhase gamePhase = gameState.getGamePhase();
        int turnWhenExpires = gameState.getCurrentTurn() + 1;
        if (gamePhase.equals(GamePhase.YOU_ACTION)) {
            gameState.setYourCurrentResources(gameState.getYourCurrentResources() - 2);
            gamePhase = GamePhase.ENEMY_PREPARE;
        }
        if (gamePhase.equals(GamePhase.ENEMY_ACTION)) {
            gameState.setEnemyCurrentResources(gameState.getEnemyCurrentResources() - 2);
            gamePhase = GamePhase.YOU_PREPARE;
            turnWhenExpires++;
        }
        card.getEffects().add(new InLoveEffect(turnWhenExpires, gamePhase));
    }
}