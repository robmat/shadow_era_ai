package edu.bator.cards;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import edu.bator.cards.enums.CardEnums;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import edu.bator.ui.cards.CardUiHelper;
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
                        EventHandler<MouseEvent> onMouseClicked = new AttachmentTargetClickedEvent(gameState, card, stage, this);
                        CardUiHelper.paintCardOnDialogGridPane(gameState, gridPane, index, card, onMouseClicked);
                    });
        }, gameState);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return card.getAttachments().stream().noneMatch(attachment -> Objects.equals(getName(), attachment.getName()));
    }

    public int armorModifier() {
        return 0;
    }

    private class AttachmentTargetClickedEvent implements EventHandler<MouseEvent> {

        GameState gameState;
        Card target;
        Stage stage;
        Attachment attachment;

        AttachmentTargetClickedEvent(GameState gameState, Card target, Stage stage, Attachment attachment) {
            this.gameState = gameState;
            this.target = target;
            this.stage = stage;
            this.attachment = attachment;
        }

        @Override
        public void handle(MouseEvent event) {
            target.getAttachments().add(Attachment.this);
            new GameEngine().decreaseCurrentPlayerResources(gameState, getResourceCost());
            if (!gameState.currentYourHandBasedOnPhase().remove(Attachment.this)) {
                throw new IllegalStateException("Attachment casted but not in casters hand?");
            }
            gameState.currentYourHandBasedOnPhase().forEach(card -> card.determineCastable(gameState));
            gameState.resetPossibleAbiltyTargets();
            gameState.resetPossibleAttackTargets();
            stage.close();
        }
    }
}
