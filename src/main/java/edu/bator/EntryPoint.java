package edu.bator;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import edu.bator.ui.GamePainter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import static org.apache.log4j.Logger.getLogger;

public class EntryPoint extends Application {

    private static final Logger log = getLogger(EntryPoint.class);
    private GameState gameState = new GameState();
    private GamePainter gamePainter = new GamePainter();

    public static void main(String[] args) {
        log.info("Starting.");
        launch(args);
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

        mainGridPane.add(gamePainter.getEnemyDeck(), 0, 0);
        mainGridPane.add(gamePainter.getEnemyHand(), 1, 0);
        mainGridPane.add(gamePainter.getEnemyResources(), 2, 0);

        mainGridPane.add(gamePainter.getEnemyHero(), 0, 1);
        mainGridPane.add(gamePainter.getEnemySupport(), 1, 1);
        mainGridPane.add(gamePainter.getEnemyGraveyard(), 2, 1);

        mainGridPane.add(gamePainter.getEnemyAllies(), 1, 2);

        mainGridPane.add(gamePainter.getYourDeck(), 0, 5);
        mainGridPane.add(gamePainter.getYourHand(), 1, 5);
        mainGridPane.add(gamePainter.getYourResources(), 2, 5);

        mainGridPane.add(gamePainter.getYourHero(), 0, 4);
        mainGridPane.add(gamePainter.getYourSupport(), 1, 4);
        mainGridPane.add(gamePainter.getYourGraveyard(), 2, 4);

        mainGridPane.add(gamePainter.getYourAllies(), 1, 3);

        mainGridPane.add(gamePainter.getEndTurnButton(), 0, 6);
        mainGridPane.add(gamePainter.getSkipSacrificeButton(), 1, 6);

        mainGridPane.add(gamePainter.getLoadButton(), 0, 7);
        mainGridPane.add(gamePainter.getSaveButton(), 1, 7);

        primaryStage.show();

        gameState.init();
        gameState.setGamePainter(gamePainter);
        gameState.repaint();

        new GameEngine().checkGameState(gameState);

        log.info("Started.");

        gamePainter.getLoadButton().setOnMouseClicked((event) -> {
            try {
                gameState = new ObjectMapper().readValue(new File("save.json"), GameState.class);
                gameState.setGamePainter(gamePainter);
                gameState.repaint();
            } catch (Exception e) {
                log.error("Load crashed.", e);
            }
        });

        gamePainter.getSaveButton().setOnMouseClicked((event) -> {
            try {
                new ObjectMapper().writeValue(new File("save.json"), gameState);
            } catch (Exception e) {
                log.error("Save crashed.", e);
            }
        });
    }
}
