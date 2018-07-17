package edu.bator;

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

        primaryStage.show();

        gameState.init();
        gameState.setGamePainter(gamePainter);
        gamePainter.paint(gameState);

        new GameEngine().checkGameState(gameState);

        log.info("Started.");
    }

    private void prepareGame() {


    }
}
