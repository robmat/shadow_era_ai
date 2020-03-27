package edu.bator.cards.done;

import static java.util.Objects.nonNull;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import edu.bator.ui.cards.CardUiHelper;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SmashingBlow extends Ability {

  public SmashingBlow() {
  }

  public SmashingBlow(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public void determineCastable(GameState gameState) {
    super.determineCastable(gameState);
    if (isCastable()) {
      boolean enemy =
          gameState.enemyAction() && (nonNull(gameState.getYourHero().getWeapon()) || nonNull(
              gameState.getYourHero().getArmor()));
      boolean you =
          gameState.yourAction() && (nonNull(gameState.getEnemyHero().getWeapon()) || nonNull(
              gameState.getEnemyHero().getArmor()));
      setCastable(you || enemy);
    }
  }

  @Override
  public void wasCasted(GameState gameState) {
    super.wasCasted(gameState);
    BiConsumer<Stage, GridPane> consumer = (stage, gridPane) -> {
      AtomicInteger index = new AtomicInteger(0);
      Stream<Card> cardStream = Stream.empty();
      if (gameState.yourAction()) {
        cardStream = Stream
            .of(gameState.getEnemyHero().getWeapon(), gameState.getEnemyHero().getArmor())
            .filter(Objects::nonNull);
      }
      if (gameState.enemyAction()) {
        cardStream = Stream
            .of(gameState.getYourHero().getWeapon(), gameState.getYourHero().getArmor())
            .filter(Objects::nonNull);
      }
      cardStream
          .forEach(card -> {
            EventHandler<MouseEvent> smashingBlowClickedEvent = new SmashingBlowClickedEvent(
                gameState, card, stage, this);
            CardUiHelper.paintCardOnDialogGridPane(gameState, gridPane, index, card,
                smashingBlowClickedEvent);
          });
    };
    CardUiHelper.showDialog(consumer, gameState);
  }

  private class SmashingBlowClickedEvent implements EventHandler<MouseEvent> {

    private final GameState gameState;
    private final Card target;
    private final Stage stage;
    private final SmashingBlow smashingBlow;

    SmashingBlowClickedEvent(GameState gameState, Card target, Stage stage,
        SmashingBlow smashingBlow) {
      this.gameState = gameState;
      this.target = target;
      this.stage = stage;
      this.smashingBlow = smashingBlow;
    }

    @Override
    public void handle(MouseEvent event) {
      new GameEngine().cardDied(target, gameState);
      new GameEngine().cardDied(smashingBlow, gameState);
      smashingBlow.applyAbility(target, gameState);
      stage.close();
    }
  }
}
