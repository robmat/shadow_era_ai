package edu.bator;

import edu.bator.cards.AllCardsSet;
import edu.bator.cards.Card;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class AllCardFromCardListPresentWithProperDescription {
    @Test
    public void should() throws IOException {
        List<Card> expected = CardParser.getCards();
        List<Card> actual = new AllCardsSet().getAllCards();

        expected.forEach(expectedCard -> {
            assertTrue(expectedCard.getName() + " not found", actual.stream().anyMatch(actualCard -> Objects.equals(actualCard.getName(), expectedCard.getName())));
            assertTrue(expectedCard.getName() + " description error", actual.stream().anyMatch(actualCard -> Objects.equals(actualCard.getDescription(), expectedCard.getDescription())));
        });
    }
}
