package edu.bator.cards.done;

import edu.bator.EntryPoint;
import edu.bator.cards.Artifact;
import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import edu.bator.ui.CardPainter;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    boolean cardIsAWeapon = card.cardIsAWeapon();
    if (cardIsAWeapon &&
        card.equals(gameState.getYourHero().getWeapon()) &&
        gameState.getYourSupport().contains(this)) {
      return 1;
    }
    if (cardIsAWeapon &&
        card.equals(gameState.getEnemyHero().getWeapon()) &&
        gameState.getEnemySupport().contains(this)) {
      return 1;
    }
    return 0;
  }

  @Override
  public boolean hasAbilityToUse(GameState gameState) {
    return isYou(gameState) || isEnemy(gameState);
  }

  @Override
  public void abilityClickEvent(GameState gameState) {
    Stage dialog = new Stage();
    dialog.initOwner(EntryPoint.primaryStage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    GridPane cardsGrid = new GridPane();
    Scene scene = new Scene(cardsGrid);
    dialog.setScene(scene);

    if (isYou(gameState)) {
      AtomicInteger index = new AtomicInteger(0);
      gameState.getYourGraveyard()
          .stream()
          .filter(Card::cardIsAWeapon)
          .forEach(card -> {
            GridPane cardGrid = new CardPainter()
                .paint(card, cardsGrid, index.getAndIncrement(), gameState);
            Button button = new Button("Target");
            button.setOnMouseClicked(new ReserveWeaponClickedEvent(gameState, card));
            cardGrid.add(button, 0, 1);
          });
    }
    if (isEnemy(gameState)) {
      AtomicInteger index = new AtomicInteger(0);
      gameState.getEnemyGraveyard()
          .stream()
          .filter(Card::cardIsAWeapon)
          .forEach(card -> {
            GridPane cardGrid = new CardPainter()
                .paint(card, cardsGrid, index.getAndIncrement(), gameState);
            Button button = new Button("Target");
            button.setOnMouseClicked(new ReserveWeaponClickedEvent(gameState, card));
            cardGrid.add(button, 0, 1);
          });
    }

    dialog.showAndWait();
  }

  private boolean isEnemy(GameState gameState) {
    return GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
        gameState.getEnemyGraveyard().stream().anyMatch(Card::cardIsAWeapon) &&
        gameState.getEnemySupport().contains(this);
  }

  private boolean isYou(GameState gameState) {
    return GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
        gameState.getYourGraveyard().stream().anyMatch(Card::cardIsAWeapon) &&
        gameState.getYourSupport().contains(this);
  }

  @Override
  public void applyAbility(Card target, GameState gameState) {
    super.applyAbility(target, gameState);
  }

  public class ReserveWeaponClickedEvent implements EventHandler<MouseEvent> {

    private final GameState gameState;
    private final Card target;

    ReserveWeaponClickedEvent(GameState gameState, Card target) {
      this.gameState = gameState;
      this.target = target;
    }

    @Override
    public void handle(MouseEvent event) {
      target.resetAttackAnHp();
      target.setBaseAttack(target.getBaseAttack() + 1);
      if (gameState.getYourGraveyard().remove(target)) {
        gameState.getYourHero().setWeapon(target);
      }
      if (gameState.getEnemyGraveyard().remove(target)) {
        gameState.getEnemyHero().setWeapon(target);
      }
    }
  }
}
