package edu.bator.ui.menu.events;

import edu.bator.EntryPoint;
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
  }
}
