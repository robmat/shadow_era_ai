package edu.bator.ui.cards;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import edu.bator.EntryPoint;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CardUiHelper {

    public static void showDialog(BiConsumer<Stage, GridPane> consumer) {
        Stage dialog = new Stage();
        dialog.initOwner(EntryPoint.primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        GridPane cardsGrid = new GridPane();
        Scene scene = new Scene(cardsGrid);
        dialog.setScene(scene);
        consumer.accept(dialog, cardsGrid);
        dialog.showAndWait();
    }

    public static void paintCardOnDialogGridPane(GameState gameState, GridPane gridPane, AtomicInteger index, Card card, EventHandler<MouseEvent> onMouseClicked) {
        GridPane cardGrid = new CardPainter()
                .paint(card, gridPane, index.getAndIncrement(), gameState);
        Button button = new Button("Target");
        button.setOnMouseClicked(onMouseClicked);
        cardGrid.add(button, 0, 2);
    }
}
