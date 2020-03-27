package edu.bator.cards;

import edu.bator.cards.util.AttachmentExpireUtil;
import edu.bator.game.GameState;
import edu.bator.ui.cards.CardUiHelper;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Attachment extends Card {

  public Attachment() {
  }

  public Attachment(Card cloneFrom) {
    super(cloneFrom);
  }

  @Override
  public void determineCastable(GameState gameState) {
    super.determineCastable(gameState);
    if (isCastable()) {
      setCastable(
          gameState.allCardsInPlay()
              .stream()
              .anyMatch(c -> ableToApplyAbilityTo(c, gameState))
      );
    }
  }

  @Override
  public void wasCasted(GameState gameState) {
    CardUiHelper.showDialog((stage, gridPane) -> {
      AtomicInteger index = new AtomicInteger(0);
      gameState.allCardsInPlay()
          .stream()
          .filter(card -> ableToApplyAbilityTo(card, gameState))
          .forEach(card -> {
            EventHandler<MouseEvent> onMouseClicked = new AttachmentTargetClickedEvent(gameState,
                card, stage, this);
            CardUiHelper
                .paintCardOnDialogGridPane(gameState, gridPane, index, card, onMouseClicked);
          });
    }, gameState);
  }

  @Override
  public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
    return card.getAttachments().stream()
        .noneMatch(attachment -> Objects.equals(getName(), attachment.getName()));
  }

  public int armorModifier() {
    return 0;
  }

  public int durabilityInCombatLost() {
    return 0;
  }

  protected void attachedTo(Card target, GameState gameState) {

  }

  @Override
  public void gamePhaseChangeEvent(GameState gameState) {
    if (this instanceof Expirable) {
      AttachmentExpireUtil.expireCard(gameState, (Expirable) this);
    }
  }

  private class AttachmentTargetClickedEvent implements EventHandler<MouseEvent> {

    GameState gameState;
    Card target;
    Stage stage;

    Attachment attachment;

    AttachmentTargetClickedEvent(GameState gameState, Card target, Stage stage,
        Attachment attachment) {
      this.gameState = gameState;
      this.target = target;
      this.stage = stage;
      this.attachment = attachment;
    }

    @Override
    public void handle(MouseEvent event) {
      Attachment attachment = Attachment.this;
      target.getAttachments().add(attachment);
      attachment.attachedTo(target, gameState);
      CardUiHelper.closeDialogDetermineCastableDecreaseResources(attachment, gameState, stage);
    }

  }
}
