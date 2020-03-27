package edu.bator.cards;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

import edu.bator.cards.util.BonusUtil;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import java.util.Objects;
import java.util.function.BiConsumer;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Hero extends Card {

  public Hero(Card cloneFrom) {
    super(cloneFrom);
  }

  public boolean canAttack(GameState gameState) {
    return isAttackReadied() && Objects.nonNull(weapon) && weapon.getCurrentHp(gameState) > 0
        && weapon.getAttack(gameState) > 0;
  }

  @Override
  public void cardHasDiedEvent(Card card, GameState gameState) {
    ofNullable(weapon).ifPresent(w -> w.cardHasDiedEvent(card, gameState));
    ofNullable(armor).ifPresent(a -> a.cardHasDiedEvent(card, gameState));
  }

  @Override
  public void attackTarget(GameState gameState, Card target) {
    Card attackSource = this;
    BiConsumer<GameState, Card> attackEvent = (gameState1, card) -> {
      Card weapon = attackSource.getWeapon();
      if (nonNull(weapon) && weapon.getAttack(gameState) > 0) {
        target.setCurrentHp(target.currentHpWithoutBonus() - weapon.getAttack(gameState1));
        reduceWeaponHp(gameState1, weapon);
      }
    };
    attackTarget(attackEvent, target, gameState);
  }

  @Override
  public void attackedBy(BiConsumer<GameState, Card> attackEvent, Card source,
      GameState gameState) {
    int hpBefore = getCurrentHp(gameState);
    int hpWithoutBonusBefore = currentHpWithoutBonus();
    super.attackedBy(attackEvent, source, gameState);
    Armor armor = getArmor();
    if (getCurrentHp(gameState) < hpBefore && nonNull(armor)) {
      int defence = armor.getBaseDefence() + BonusUtil.attackBonus(gameState, this);
      setCurrentHp(hpBefore - getCurrentHp(gameState) > defence ? currentHpWithoutBonus() + defence
          : hpWithoutBonusBefore);
      armor.setCurrentHp(armor.getCurrentHp(gameState) - 1 + armor.getAttachments().stream()
          .map(Attachment::durabilityInCombatLost).reduce(0, (a, b) -> a + b));
      if (armor.cardIsDead()) {
        new GameEngine().cardDied(armor, gameState);
      }
    }
    if (nonNull(armor)) {
      armor.armorEffectDuringAttack(this, source, gameState);
    }
  }

  @Override
  public Integer getCurrentHp(GameState gameState) {
    return super.getCurrentHp(gameState) + BonusUtil.hpBonus(gameState, this);
  }
}
