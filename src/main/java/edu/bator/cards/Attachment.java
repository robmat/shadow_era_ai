package edu.bator.cards;

import java.util.concurrent.atomic.AtomicInteger;

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
        setCastable(
                gameState.allCardsInPlay()
                        .stream()
                        .anyMatch(c -> ableToApplyAbilityTo(c, gameState))
        );
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


    private class AttachmentTargetClickedEvent implements EventHandler<MouseEvent> {

        GameState gameState;
        Card target;
        Stage stage;
        Attachment attachmen;

        public AttachmentTargetClickedEvent(GameState gameState, Card target, Stage stage, Attachment attachmen) {
            this.gameState = gameState;
            this.target = target;
            this.stage = stage;
            this.attachmen = attachmen;
        }

        @Override
        public void handle(MouseEvent event) {
            target.getAttachments().add(Attachment.this);
            new GameEngine().decreaseCurrentPlayerResources(gameState, getResourceCost());
            new GameEngine().moveToGraveYard(Attachment.this, gameState);
            stage.close();
        }
    }
}
