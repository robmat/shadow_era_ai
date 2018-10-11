package edu.bator.cards.done;

import edu.bator.cards.Artifact;
import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import java.util.Objects;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ReserveWeapon extends Artifact {

  public ReserveWeapon() {
  }

  public ReserveWeapon(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public void supportIsCast(GameState gameState) {
    super.supportIsCast(gameState);
  }

  @Override
  public Integer modifiesAttack(Card card, GameState gameState) {
    if (card.cardIsAWeapon() &&
        card.equals(gameState.getYourHero().getWeapon()) &&
        gameState.getYourSupport().contains(this)) {
      return 1;
    }
    if (card.cardIsAWeapon() &&
        Objects.nonNull(card.getWeapon()) &&
        card.equals(gameState.getEnemyHero().getWeapon()) &&
        gameState.getEnemySupport().contains(this)) {
      return 1;
    }
    return 0;
  }

  @Override
  public boolean hasAbilityToUse(GameState gameState) {
    return (GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
        gameState.getYourGraveyard().stream().anyMatch(Card::cardIsAWeapon)) ||
        (GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
            gameState.getEnemyGraveyard().stream().anyMatch(Card::cardIsAWeapon));
  }
}
