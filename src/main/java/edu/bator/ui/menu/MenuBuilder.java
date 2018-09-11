package edu.bator.ui.menu;

import edu.bator.EntryPoint;
import edu.bator.ui.menu.events.NewRandomDecksGameEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuBuilder {

  EntryPoint entryPoint;

  public void build(MenuBar menuBar, EntryPoint entryPoint) {
    Menu game = new Menu("Game");
    menuBar.getMenus().add(game);
    MenuItem newRandomGame = new MenuItem("New random decks game.");
    game.getItems().add(newRandomGame);
    newRandomGame.setOnAction(new NewRandomDecksGameEvent(entryPoint));
  }
}
