package edu.bator.ui.menu;

import edu.bator.EntryPoint;
import edu.bator.ui.menu.events.NewRandomDecksGameEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class MenuBuilder {

  EntryPoint entryPoint;

  public void build(MenuBar menuBar, EntryPoint entryPoint) {
    Menu game = new Menu("Game");
    menuBar.getMenus().add(game);

    MenuItem newRandomGame = new MenuItem("New random decks game.");
    game.getItems().add(newRandomGame);
    newRandomGame.setOnAction(new NewRandomDecksGameEvent(entryPoint));
    newRandomGame.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

    MenuItem exit = new MenuItem("Exit.");
    game.getItems().add(exit);
    exit.setOnAction(event -> System.exit(0));
  }
}
