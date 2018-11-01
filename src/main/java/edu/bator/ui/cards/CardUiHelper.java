package edu.bator.ui.cards;

import java.util.function.BiConsumer;

import edu.bator.EntryPoint;
import javafx.scene.Scene;
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
}
