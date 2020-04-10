package edu.bator.cards.done;

import edu.bator.cards.Ability;
import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import edu.bator.ui.cards.CardUiHelper;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SeverTies extends Ability {

  public SeverTies() {
  }

  public SeverTies(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public void determineCastable(GameState gameState) {
    super.determineCastable(gameState);
    if (isCastable()) {
      setCastable(gameState.allCardsInPlay().stream().anyMatch(card -> !card.getAttachments().isEmpty()));
    }
  }

  @Override
  public void wasCasted(GameState gameState) {
    super.wasCasted(gameState);
    BiConsumer<Stage, GridPane> consumer = (stage, gridPane) -> {
      AtomicInteger index = new AtomicInteger(0);
      Set<Card> targets = new HashSet<>();
      gameState.allCardsInPlay().forEach(card -> targets.addAll(card.getAttachments()));
      Stream<Card> cardStream = targets.stream();

      cardStream
          .forEach(card -> {
            EventHandler<MouseEvent> smashingBlowClickedEvent = new SeverTiesClickedEvent(
                gameState, card, stage, this);
            CardUiHelper.paintCardOnDialogGridPane(gameState, gridPane, index, card,
                smashingBlowClickedEvent);
          });
    };
    CardUiHelper.showDialog(consumer, gameState);
  }

  private static class SeverTiesClickedEvent implements EventHandler<MouseEvent> {

    private final GameState gameState;
    private final Card target;
    private final Stage stage;
    private final SeverTies severTies;

    SeverTiesClickedEvent(GameState gameState, Card target, Stage stage,
        SeverTies severTies) {
      this.gameState = gameState;
      this.target = target;
      this.stage = stage;
      this.severTies = severTies;
    }

    @Override
    public void handle(MouseEvent event) {
      new GameEngine().cardDied(target, gameState);
      new GameEngine().cardDied(severTies, gameState);
      severTies.applyAbility(target, gameState);
      stage.close();
    }
  }
}
