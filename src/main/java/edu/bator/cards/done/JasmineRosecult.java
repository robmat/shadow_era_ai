package edu.bator.cards.done;

import java.util.function.BiConsumer;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.cards.effects.InLoveEffect;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.NoArgsConstructor;

@NoArgsConstructor
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
        boolean possibleAllyTarget = calculatePossibleAllyTarget(gameState);
        return card.cardIsAnAlly() && gameState.currentEnemyAlliesBasedOnPhase().contains(card) && possibleAllyTarget;
    }

    @Override
    public void applyAbility(Card card, GameState gameState) {
        BiConsumer<Card, GameState> abilityFunction = (card1, gameState1) -> {
            GamePhase gamePhase = gameState1.getGamePhase();
            int turnWhenExpires = gameState1.getCurrentTurn() + 1;
            if (gamePhase.equals(GamePhase.YOU_ACTION)) {
                gameState1.setYourCurrentResources(gameState1.getYourCurrentResources() - 2);
                gamePhase = GamePhase.ENEMY_PREPARE;
            }
            if (gamePhase.equals(GamePhase.ENEMY_ACTION)) {
                gameState1.setEnemyCurrentResources(gameState1.getEnemyCurrentResources() - 2);
                gamePhase = GamePhase.YOU_PREPARE;
                turnWhenExpires++;
            }
            card1.getEffects().add(new InLoveEffect(turnWhenExpires, gamePhase));
        };
       card.applyAbility(abilityFunction, gameState);
    }
}