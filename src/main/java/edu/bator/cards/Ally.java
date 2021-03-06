package edu.bator.cards;

import static java.util.Objects.nonNull;

import edu.bator.cards.util.ArmorUtil;
import edu.bator.cards.util.BonusUtil;
import edu.bator.game.GameState;
import java.util.function.BiConsumer;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ally extends Card {

  public enum Affinity {
    UNDEAD, WULVEN, TWILIGHT, HOMUNCULUS, TEMPLAR, RAVAGER, ALDMOR
  }

  public Ally(Card cloneFrom) {
    super(cloneFrom);
  }

  public void attackTarget(GameState gameState, Card target) {
    Card attackSource = this;
    BiConsumer<GameState, Card> attackEvent = (stateOfTheGame, card) -> {
      if (nonNull(attackSource.getAttack(stateOfTheGame)) && nonNull(
          target.getCurrentHp(gameState))) {
        target.setCurrentHp(target.currentHpWithoutBonus() - ArmorUtil
            .attachmentModifiesAttack(target, attackSource, gameState));
      }
    };
    attackTarget(attackEvent, target, gameState);
  }

  @Override
  public Integer getAttack(GameState gameState) {
    int attack = super.getAttack(gameState) + BonusUtil.attackBonus(gameState, this);
    return Math.max(attack, 0);
  }

  @Override
  public Integer getCurrentHp(GameState gameState) {
    return super.getCurrentHp(gameState) + BonusUtil.hpBonus(gameState, this);
  }
}
