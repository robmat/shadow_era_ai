package edu.bator.cards;

import static edu.bator.cards.util.ArmorUtil.attachmentModifiesAbilityDamage;

import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import java.util.Objects;
import java.util.function.BiConsumer;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ability extends Card {

  public Ability(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public void applyAbility(Card target, GameState gameState) {
    target.resetFlags();
    new GameEngine().decreaseCurrentPlayerResources(gameState, getResourceCost());
    super.applyAbility(target, gameState);
  }

  protected void applyAbilityDamage(Card target, GameState gameState, int abilityDamage) {
    BiConsumer<Card, GameState> abilityFunction = (target1, gameState1) -> {
      if (target1.cardIsAnAlly() && Objects.nonNull(target1.getCurrentHp(gameState))) {
        target1.setCurrentHp(
            target1.currentHpWithoutBonus() - attachmentModifiesAbilityDamage(target, abilityDamage,
                gameState));
      }
    };
    target.abilityAppliedToMe(abilityFunction, gameState);
    if (target.cardIsDead()) {
      new GameEngine().cardDied(target, gameState);
    }
  }
}
