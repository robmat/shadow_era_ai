package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class DirkSaber extends Ally {

  public DirkSaber() {
  }

  public DirkSaber(Card cloneFrom) {
    super(cloneFrom);
  }
}
