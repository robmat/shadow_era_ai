package edu.bator;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bator.cards.AllCardsSet;
import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import edu.bator.ui.GamePainter;
import edu.bator.ui.LogginExceptionHandler;
import edu.bator.ui.events.SkipSacrificeClickedEvent;
import edu.bator.ui.events.TurnSkipClickedEvent;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import static org.apache.log4j.Logger.getLogger;

public class EntryPoint extends Application {

    private static final Logger log = getLogger(EntryPoint.class);
    private GameState gameState = new GameState();
    private GamePainter gamePainter = new GamePainter();
    public static ObjectMapper objectJsonMapper = new ObjectMapper();

    public static void main(String[] args) {
        log.info("Starting.");
        objectJsonMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Thread.setDefaultUncaughtExceptionHandler(new LogginExceptionHandler());
        try {
            primaryStage.getIcons().add(new Image(getClass().getResource("/icon.png").toURI().toURL().toString()));
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Error loading icon.", e);
        }
        primaryStage.setTitle("Shadow Era.");
        GridPane mainGridPane = new GridPane();
        mainGridPane.setPadding(new Insets(5));
        ScrollPane scrollPane = new ScrollPane(mainGridPane);
        Scene scene = new Scene(scrollPane);
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

        EventHandler<MouseEvent> loadEvent = (event) -> {
            try {
                AllCardsSet allCardsSet = gameState.getAllCardsSet();
                gameState = objectJsonMapper.readValue(new File("save.json"), GameState.class);
                gameState.setGamePainter(gamePainter);
                gameState.setEnemyHero(allCardsSet.replaceWithImplementingCard(gameState.getEnemyHero()));
                gameState.setYourHero(allCardsSet.replaceWithImplementingCard(gameState.getYourHero()));

                gameState.getEnemyHero().setWeapon(allCardsSet.replaceWithImplementingCard(gameState.getEnemyHero().getWeapon()));
                gameState.getYourHero().setWeapon(allCardsSet.replaceWithImplementingCard(gameState.getYourHero().getWeapon()));

                replaceCardsWithImplementors(allCardsSet, gameState.getEnemyAllies());
                replaceCardsWithImplementors(allCardsSet, gameState.getEnemyDeck());
                replaceCardsWithImplementors(allCardsSet, gameState.getEnemyGraveyard());
                replaceCardsWithImplementors(allCardsSet, gameState.getEnemyHand());
                replaceCardsWithImplementors(allCardsSet, gameState.getEnemyResources());
                replaceCardsWithImplementors(allCardsSet, gameState.getEnemySupport());

                replaceCardsWithImplementors(allCardsSet, gameState.getYourAllies());
                replaceCardsWithImplementors(allCardsSet, gameState.getYourDeck());
                replaceCardsWithImplementors(allCardsSet, gameState.getYourGraveyard());
                replaceCardsWithImplementors(allCardsSet, gameState.getYourHand());
                replaceCardsWithImplementors(allCardsSet, gameState.getYourResources());
                replaceCardsWithImplementors(allCardsSet, gameState.getYourSupport());

                gameState.repaint();
            } catch (Exception e) {
                log.error("Load crashed.", e);
            }
        };
        gamePainter.getLoadButton().setOnMouseClicked(loadEvent);

        EventHandler<MouseEvent> saveEvent = (event) -> {
            try {
                objectJsonMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File("save.json"), gameState);
            } catch (Exception e) {
                log.error("Save crashed.", e);
            }
        };
        gamePainter.getSaveButton().setOnMouseClicked(saveEvent);

        scene.getAccelerators().put(new KeyCharacterCombination("l"), () -> loadEvent.handle(null));
        scene.getAccelerators().put(new KeyCharacterCombination("s"), () -> saveEvent.handle(null));
        scene.getAccelerators().put(new KeyCharacterCombination("e"), () -> new TurnSkipClickedEvent(gameState).handle(null));
        scene.getAccelerators().put(new KeyCharacterCombination("s"), () -> new SkipSacrificeClickedEvent(gameState).handle(null));
    }

    private void replaceCardsWithImplementors(AllCardsSet allCardsSet, LinkedList<Card> originalCards)
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (int i = 0; i < originalCards.size(); i++) {
            originalCards.set(i, allCardsSet.replaceWithImplementingCard(originalCards.get(i)));
        }
    }
}
