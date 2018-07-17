package edu.bator;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import static org.apache.log4j.Logger.getLogger;

public class EntryPoint extends Application {

    private static final Logger log = getLogger(EntryPoint.class);
    private GameState gameState;

    public static void main(String[] args) {
        launch(args);
        log.info("Started.");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Shadow Era.");
        GridPane mainGridPane = new GridPane();
        mainGridPane.setPadding(new Insets(5));
        ScrollPane scrollPane = new ScrollPane(mainGridPane);
        Scene scene  = new Scene(scrollPane);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        mainGridPane.add(new Label("Enemy deck."), 0, 0);
        mainGridPane.add(new Label("Enemy hand."), 1, 0);
        mainGridPane.add(new Label("Enemy resources."), 2, 0);

        mainGridPane.add(new Label("Enemy hero."), 0, 1);
        mainGridPane.add(new Label("Enemy support."), 1, 1);
        mainGridPane.add(new Label("Enemy graveyard."), 2, 1);

        mainGridPane.add(new Label("Enemy allies."), 1, 2);

        mainGridPane.add(new Label("Your deck."), 0, 5);
        mainGridPane.add(new Label("Your hand."), 1, 5);
        mainGridPane.add(new Label("Your resources."), 2, 5);

        mainGridPane.add(new Label("Your hero."), 0, 4);
        mainGridPane.add(new Label("Your support."), 1, 4);
        mainGridPane.add(new Label("Your graveyard."), 2, 4);

        mainGridPane.add(new Label("Your allies."), 1, 3);

        primaryStage.show();

        prepareGame();
    }

    private void prepareGame() {
        gameState = new GameState();
    }
}
