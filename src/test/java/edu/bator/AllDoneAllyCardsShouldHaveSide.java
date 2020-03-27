package edu.bator;

import edu.bator.cards.AllCardsSet;
import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import org.junit.Assert;
import org.junit.Test;

public class AllDoneAllyCardsShouldHaveSide {

  @Test
  public void should() {
    AllCardsSet allCardsSet = new AllCardsSet();
    for (Card card : allCardsSet.getAllCards()) {
      if (card instanceof Ally) {
        Assert
            .assertNotNull(card.getClass().getSimpleName() + " should have a side", card.getSide());
      }
    }
  }
}
