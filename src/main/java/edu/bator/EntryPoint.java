package edu.bator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import static org.apache.log4j.Logger.getLogger;

public class EntryPoint extends Application {

    private static final Logger log = getLogger(EntryPoint.class);

    public static void main(String[] args) {
        launch(args);
        log.info("Started.");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Shadow Era.");
        GridPane mainGridPane = new GridPane();
        ScrollPane scrollPane = new ScrollPane(mainGridPane);
        Scene scene  = new Scene(scrollPane);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        mainGridPane.add(new Label("Enemy deck."), 0, 0);
        mainGridPane.add(new Label("Enemy hand."), 1, 0);
        mainGridPane.add(new Label("Enemy resources."), 2, 0);

        primaryStage.show();
    }
}
