package edu.bator.cards.done;

import edu.bator.cards.Card;
import edu.bator.cards.Hero;
import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import java.util.function.BiConsumer;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BorisSkullcrusher extends Hero {

  public BorisSkullcrusher(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public boolean hasAbilityToUse(GameState gameState) {
    boolean shadowEnergySufficient = getShadowEnergy() >= 4;
    boolean you = GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
        gameState.getYourHero().equals(this) &&
        gameState.getEnemyAllies().stream()
            .anyMatch(ally -> ally.getResourceCost() <= 4 && ally.cardIsAnAlly());
    boolean enemy = GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
        gameState.getEnemyHero().equals(this) &&
        gameState.getYourAllies().stream()
            .anyMatch(ally -> ally.getResourceCost() <= 4 && ally.cardIsAnAlly());
    boolean abilityReadied = isAbilityReadied();
    return shadowEnergySufficient && (you || enemy) && abilityReadied;
  }

  @Override
  public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
    boolean possibleAllyTarget = calculatePossibleEnemyTargetProtectorIncluded(card, gameState);
    return card.cardIsAnAlly() &&
        card.getResourceCost() <= 4 &&
        gameState.currentEnemyAlliesBasedOnPhase().contains(card) &&
        possibleAllyTarget;
  }

  @Override
  public void applyAbility(Card card, GameState gameState) {
    BiConsumer<Card, GameState> abilityFunction = new BiConsumer<Card, GameState>() {
      @Override
      public void accept(Card card, GameState gameState) {
        if (card.cardIsAnAlly() && card.getResourceCost() <= 4) {
          new GameEngine().cardDied(card, gameState);
          setShadowEnergy(getShadowEnergy() - 4);
        }
      }
    };
    card.abilityAppliedToMe(abilityFunction, gameState);
  }
}