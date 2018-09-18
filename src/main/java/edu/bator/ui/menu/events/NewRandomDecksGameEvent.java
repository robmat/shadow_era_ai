package edu.bator.ui.menu.events;

import static com.google.common.collect.Iterables.isEmpty;
import static java.util.Objects.isNull;

import edu.bator.EntryPoint;
import edu.bator.cards.Card;
import edu.bator.cards.enums.Owner;
import edu.bator.game.GameEngine;
import edu.bator.game.GameState;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.apache.log4j.Logger;

public class NewRandomDecksGameEvent implements EventHandler<ActionEvent> {

  private static final Logger log = Logger.getLogger(NewRandomDecksGameEvent.class);

  private EntryPoint entryPoint;

  public NewRandomDecksGameEvent(EntryPoint entryPoint) {
    this.entryPoint = entryPoint;
  }

  @Override
  public void handle(ActionEvent event) {
    log.debug("New random game starting.");
    GameState gameState = new GameState();
    gameState.setGamePainter(entryPoint.getGamePainter());
    entryPoint.setGameState(gameState);

    List<Card> cardsYou = new ArrayList<>();
    EntryPoint.allCardsSet.getAllCards()
        .stream()
        .filter(card -> isDone(card.getName()))
        .forEach(card -> {
          if (card.cardIsAHero()) {
            cardsYou.add(EntryPoint.allCardsSet.cloneByName(card.getName(), Owner.YOU));
          } else {
            for (int fourCards = 0; fourCards < 4; fourCards ++) {
              cardsYou.add(EntryPoint.allCardsSet.cloneByName(card.getName(), Owner.YOU));
            }
          }
        });

    while (isNull(gameState.getYourHero())) {
      Card randomCard = cardsYou.get(new Random().nextInt(cardsYou.size()));
      if (randomCard.cardIsAHero()) {
        gameState.setYourHero(randomCard);
        cardsYou.remove(randomCard);
      }
    }

    while (gameState.getYourDeck().size() < 40) {
      Card randomCard = cardsYou.get(new Random().nextInt(cardsYou.size()));
      if (isNull(randomCard.getSide()) || randomCard.getSide()
          .equals(gameState.getYourHero().getSide())) {
        if (isEmpty(randomCard.getAvailableForHeroClasses()) || randomCard
            .getAvailableForHeroClasses().contains(gameState.getYourHero().getHeroClass())) {
          gameState.getYourDeck().add(randomCard);
          cardsYou.remove(randomCard);
        }
      }
    }

    List<Card> cardsEnemy = new ArrayList<>();
    EntryPoint.allCardsSet.getAllCards()
        .stream()
        .filter(card -> isDone(card.getName()))
        .forEach(card -> {
          if (card.cardIsAHero()) {
            cardsEnemy.add(EntryPoint.allCardsSet.cloneByName(card.getName(), Owner.YOU));
          } else {
            cardsEnemy.add(EntryPoint.allCardsSet.cloneByName(card.getName(), Owner.YOU));
            cardsEnemy.add(EntryPoint.allCardsSet.cloneByName(card.getName(), Owner.YOU));
            cardsEnemy.add(EntryPoint.allCardsSet.cloneByName(card.getName(), Owner.YOU));
            cardsEnemy.add(EntryPoint.allCardsSet.cloneByName(card.getName(), Owner.YOU));
          }
        });

    while (isNull(gameState.getEnemyHero())) {
      Card randomCard = cardsEnemy.get(new Random().nextInt(cardsEnemy.size()));
      if (randomCard.cardIsAHero()) {
        gameState.setEnemyHero(randomCard);
        cardsEnemy.remove(randomCard);
      }
    }

    while (gameState.getEnemyDeck().size() < 40) {
      Card randomCard = cardsEnemy.get(new Random().nextInt(cardsEnemy.size()));
      if (isNull(randomCard.getSide()) || randomCard.getSide()
          .equals(gameState.getEnemyHero().getSide())) {
        if (isEmpty(randomCard.getAvailableForHeroClasses()) || randomCard
            .getAvailableForHeroClasses().contains(gameState.getEnemyHero().getHeroClass())) {
          gameState.getEnemyDeck().add(randomCard);
          cardsEnemy.remove(randomCard);
        }
      }
    }

    for (int i = 0; i < 6; i++) {
      gameState.getYourHand().add(gameState.getYourDeck().pop());
      gameState.getEnemyHand().add(gameState.getEnemyDeck().pop());
    }

    new GameEngine().checkGameState(gameState);

    gameState.repaint();
  }

  private boolean isDone(String name) {
    return Files.isRegularFile(Paths.get("src", "main", "java", "edu", "bator", "cards", "done",
        name.replaceAll("[ :'!,-]", "") + ".java"));
  }

  private boolean deckContainsFourOfType(List<Card> deck, Card card) {
    return deck.stream()
        .filter(c -> Objects.equals(c.getName(), card.getName()))
        .count() >= 4;
  }
}
