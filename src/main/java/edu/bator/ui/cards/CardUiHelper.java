package edu.bator.ui.cards;

import edu.bator.EntryPoint;
import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class CardUiHelper {

    public static void showDialog(BiConsumer<Stage, GridPane> consumer, GameState gameState) {
        Stage dialog = new Stage();
        dialog.initOwner(EntryPoint.primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        GridPane cardsGrid = new GridPane();
        Scene scene = new Scene(cardsGrid);
        dialog.setScene(scene);
        consumer.accept(dialog, cardsGrid);
        dialog.setOnCloseRequest(event -> {
            gameState.setAbilitySource(null);
            gameState.resetPossibleAbilityTargets();
            gameState.resetPossibleAttackTargets();
        });
        dialog.showAndWait();
    }

    public static void paintCardOnDialogGridPane(GameState gameState, GridPane gridPane, AtomicInteger index, Card card, EventHandler<MouseEvent> onMouseClicked) {
        GridPane cardGrid = new CardPainter()
                .paint(card, gridPane, index.getAndIncrement(), gameState);
        Button button = new Button("Target");
        button.setOnMouseClicked(onMouseClicked);

        cardGrid.getChildren()
                .stream()
                .filter(child -> child instanceof Button)
                .collect(Collectors.toList())
                .forEach(child -> cardGrid.getChildren().remove(child));

        cardGrid.add(button, 0, 3, 2, 1);
    }

    public static void closeDialogDetermineCastableDecreaseResources(Card card, GameState gameState, Stage stage) {
        new GameEngine().decreaseCurrentPlayerResources(gameState, card.getResourceCost());
        if (!gameState.currentYourHandBasedOnPhase().remove(card)) {
            throw new IllegalStateException("Card casted but not in casters hand?");
        }
        gameState.currentYourHandBasedOnPhase().forEach(castable -> castable.determineCastable(gameState));
        gameState.resetPossibleAbilityTargets();
        gameState.resetPossibleAttackTargets();
        stage.close();
        gameState.repaint();
    }
}
