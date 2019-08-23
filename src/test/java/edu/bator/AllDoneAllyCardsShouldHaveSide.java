package edu.bator;

import edu.bator.cards.AllCardsSet;
import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class AllDoneAllyCardsShouldHaveSide {
    @Test
    public void should() {
        AllCardsSet allCardsSet = new AllCardsSet();
        for (Card card : allCardsSet.getAllCards()) {
            if (card instanceof Ally) {
                Assert.assertNotNull(card.getClass().getSimpleName() + " should have a side", card.getSide());
            }
        }
    }
}
