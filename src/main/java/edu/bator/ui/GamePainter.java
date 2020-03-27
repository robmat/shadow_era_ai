package edu.bator.ui;

import static java.util.Objects.nonNull;

import edu.bator.cards.Card;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import edu.bator.ui.cards.CardPainter;
import edu.bator.ui.events.SkipSacrificeClickedEvent;
import edu.bator.ui.events.TurnSkipClickedEvent;
import java.util.LinkedList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GamePainter {

  GridPane enemyDeck = new GridPane();
  GridPane enemyHand = new GridPane();
  GridPane enemyResources = new GridPane();
  GridPane enemyHero = new GridPane();
  GridPane enemySupport = new GridPane();
  GridPane enemyGraveyard = new GridPane();
  GridPane enemyAllies = new GridPane();
  GridPane yourDeck = new GridPane();
  GridPane yourHand = new GridPane();
  GridPane yourResources = new GridPane();
  GridPane yourHero = new GridPane();
  GridPane yourSupport = new GridPane();
  GridPane yourGraveyard = new GridPane();
  GridPane yourAllies = new GridPane();

  Button endTurnButton = new Button("End turn. (E)");
  Button skipSacrificeButton = new Button("Skip sacrifice. (S)");

  Button saveButton = new Button("Save game. (Ctrl+S)");
  Button loadButton = new Button("Load game. (Ctrl+L)");

  CardPainter cardPainter = new CardPainter();

  public void paint(GameState gameState) {
    clear();

    enemyDeck.add(new Label("Cards in deck: " + gameState.getEnemyDeck().size()), 0, 0);
    paintCardList(gameState.getEnemyHand(), enemyHand, gameState);
    enemyResources.add(new Label(
        "Resources: " + gameState.getEnemyCurrentResources() + "/" + gameState
            .enemyResourcesSize()), 0, 0);
    cardPainter.paint(gameState.getEnemyHero(), enemyHero, 0, gameState);

    LinkedList<Card> enemySupport = new LinkedList<>(gameState.getEnemySupport());
    if (nonNull(gameState.getEnemyHero().getArmor())) {
      enemySupport.add(gameState.getEnemyHero().getArmor());
    }
    if (nonNull(gameState.getEnemyHero().getWeapon())) {
      enemySupport.add(gameState.getEnemyHero().getWeapon());
    }
    paintCardList(enemySupport, this.enemySupport, gameState);

    enemyGraveyard.add(new Label("Graveyard: " + gameState.getEnemyGraveyard().size()), 0, 0);
    paintCardList(gameState.getEnemyAllies(), enemyAllies, gameState);

    yourDeck.add(new Label("Cards in deck: " + gameState.getYourDeck().size()), 0, 0);
    paintCardList(gameState.getYourHand(), yourHand, gameState);
    yourResources.add(new Label(
            "Resources: " + gameState.getYourCurrentResources() + "/" + gameState.yourResourcesSize()),
        0, 0);
    cardPainter.paint(gameState.getYourHero(), yourHero, 0, gameState);

    LinkedList<Card> yourSupport = new LinkedList<>(gameState.getYourSupport());
    if (nonNull(gameState.getYourHero().getArmor())) {
      yourSupport.add(gameState.getYourHero().getArmor());
    }
    if (nonNull(gameState.getYourHero().getWeapon())) {
      yourSupport.add(gameState.getYourHero().getWeapon());
    }
    paintCardList(yourSupport, this.yourSupport, gameState);

    yourGraveyard.add(new Label("Graveyard: " + gameState.getYourGraveyard().size()), 0, 0);
    paintCardList(gameState.getYourAllies(), yourAllies, gameState);

    endTurnButton.setVisible(GameEngine.ACTION_PHASES.contains(gameState.getGamePhase()));
    endTurnButton.setOnMouseClicked(new TurnSkipClickedEvent(gameState));

    skipSacrificeButton.setVisible(GameEngine.SACRIFICE_PHASES.contains(gameState.getGamePhase()));
    skipSacrificeButton.setOnMouseClicked(new SkipSacrificeClickedEvent(gameState));
  }

  private void clear() {
    enemyDeck.getChildren().clear();
    enemyHand.getChildren().clear();
    enemyResources.getChildren().clear();
    enemyHero.getChildren().clear();
    enemySupport.getChildren().clear();
    enemyGraveyard.getChildren().clear();
    enemyAllies.getChildren().clear();
    yourDeck.getChildren().clear();
    yourHand.getChildren().clear();
    yourResources.getChildren().clear();
    yourHero.getChildren().clear();
    yourSupport.getChildren().clear();
    yourGraveyard.getChildren().clear();
    yourAllies.getChildren().clear();
  }

  private void paintCardList(LinkedList<Card> enemyHandCardList, GridPane enemyHand,
      GameState gameState) {
    int index = 0;
    for (Card card : enemyHandCardList) {
      cardPainter.paint(card, enemyHand, index++, gameState);
    }
  }
}
