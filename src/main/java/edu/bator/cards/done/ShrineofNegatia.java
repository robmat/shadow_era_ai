package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import edu.bator.ui.cards.CardUiHelper;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ShrineofNegatia extends Ability {

  public ShrineofNegatia() {
  }

  public ShrineofNegatia(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public void wasCasted(GameState gameState) {
    new GameEngine().clearAllAbilityAndAttackTargets(gameState);
    CardUiHelper.showDialog((stage, gridPane) -> {
      AtomicInteger index = new AtomicInteger(0);
      gameState.allCardsInPlay()
          .stream()
          .filter(card -> ableToApplyAbilityTo(card, gameState))
          .forEach(card -> {
            EventHandler<MouseEvent> onMouseClicked = new ShrineofNegatia.ShrineofNegatiaTargetClickedEvent(
                gameState, card, stage);
            CardUiHelper
                .paintCardOnDialogGridPane(gameState, gridPane, index, card, onMouseClicked);
          });
    }, gameState);
  }

  @Override
  public boolean ableToApplyAbilityTo(Card target, GameState gs) {
    Stream<Card> enemyCards = Stream
        .concat(Stream.of(gs.getEnemyHero().getWeapon(), gs.getEnemyHero().getArmor()),
            gs.getEnemySupport().stream());
    Stream<Card> yourCards = Stream
        .concat(Stream.of(gs.getYourHero().getWeapon(), gs.getYourHero().getArmor()),
            gs.getYourSupport().stream());

    boolean you = gs.yourAction() &&
        enemyCards.anyMatch(card -> card.getResourceCost() <= 4 && card.equals(target)) &&
        gs.getYourHand().contains(this);
    boolean enemy = gs.enemyAction() &&
        yourCards.anyMatch(card -> card.getResourceCost() <= 4 && card.equals(target)) &&
        gs.getEnemyHand().contains(this);

    return you || enemy;
  }

  @Override
  public void applyAbility(Card target, GameState gameState) {
    if (gameState.yourAction()) {
      if (target.equals(gameState.getEnemyHero().getArmor())) {
        gameState.getEnemyHero().setArmor(null);
        gameState.getEnemyHand().add(target);
      }
      if (target.equals(gameState.getEnemyHero().getWeapon())) {
        gameState.getEnemyHero().setWeapon(null);
        gameState.getEnemyHand().add(target);
      }
      if (gameState.getEnemySupport().remove(target)) {
        gameState.getEnemyHand().add(target);
      }
    }
    if (gameState.enemyAction()) {
      if (target.equals(gameState.getYourHero().getArmor())) {
        gameState.getYourHero().setArmor(null);
        gameState.getYourHand().add(target);
      }
      if (target.equals(gameState.getYourHero().getWeapon())) {
        gameState.getYourHero().setWeapon(null);
        gameState.getYourHand().add(target);
      }
      if (gameState.getYourSupport().remove(target)) {
        gameState.getYourHand().add(target);
      }
    }
  }

  private class ShrineofNegatiaTargetClickedEvent implements EventHandler<MouseEvent> {

    Stage stage;
    GameState gameState;
    Card target;

    ShrineofNegatiaTargetClickedEvent(GameState gameState, Card target, Stage stage) {
      this.gameState = gameState;
      this.target = target;
      this.stage = stage;
    }

    @Override
    public void handle(MouseEvent event) {
      applyAbility(target, gameState);
      Card card = ShrineofNegatia.this;
      CardUiHelper.closeDialogDetermineCastableDecreaseResources(card, gameState, stage);
    }
  }
}
