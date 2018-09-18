package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import java.util.Objects;

public class AeonStormcaller extends Ally {

  public AeonStormcaller() {
  }

  public AeonStormcaller(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public boolean hasAbilityToUse(GameState gameState) {
    boolean you = GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
        gameState.getYourAllies().contains(this) &&
        gameState.getYourAllies().size() > 1 &&
        gameState.getYourCurrentResources() >= 3;
    boolean enemy = GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
        gameState.getEnemyAllies().contains(this) &&
        gameState.getEnemyAllies().size() > 1 &&
        gameState.getEnemyCurrentResources() >= 3;
    boolean abilityReadied = isAbilityReadied();
    return (you || enemy) && abilityReadied;
  }

  @Override
  public boolean ableToApplyAbilityTo(Card target, GameState gameState) {
    return !Objects.equals(this, target) && gameState.currentYourAlliesBasedOnPhase().contains(target);
  }

  @Override
  public void applyAbility(Card target, GameState gameState) {
    if (gameState.getGamePhase().equals(GamePhase.YOU_ACTION)) {
      gameState.setYourCurrentResources(gameState.getYourCurrentResources() - 3);
    }
    if (gameState.getGamePhase().equals(GamePhase.ENEMY_ACTION)) {
      gameState.setEnemyCurrentResources(gameState.getEnemyCurrentResources() - 3);
    }
    target.setCurrentHp(target.getCurrentHp() + 1);
    target.setBaseAttack(target.getBaseAttack() + 1);
  }
}