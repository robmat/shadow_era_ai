package edu.bator.cards.util;

import edu.bator.cards.Card;

public class HealingUtil {

  public static void heal(int hpToHeal, Card ally) {
    if (ally.getInitialHp() > ally.currentHpWithoutBonus()) {
      int diff = ally.getInitialHp() - ally.currentHpWithoutBonus();
      ally.setCurrentHp(
          diff >= hpToHeal ? ally.currentHpWithoutBonus() + hpToHeal : ally.getInitialHp());
    }
  }
}
