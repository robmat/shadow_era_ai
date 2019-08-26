package edu.bator;

import edu.bator.cards.AllCardsSet;
import edu.bator.cards.Card;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AllCardFromCardListPresentWithProperDescription {
    @Test
    public void should() throws IOException {
        List<Card> expected = CardParser.getCards();
        List<Card> actual = new AllCardsSet().getAllCards();

        expected.forEach(expectedCard -> {
            Optional<Card> found = actual.stream().filter(actualCard -> Objects.equals(actualCard.getName(), expectedCard.getName())).findFirst();
            assertTrue(expectedCard.getName() + " not found", found.isPresent());
            String expectedDescription = expectedCard.getDescription() != null ? expectedCard.getDescription().replace("&nbsp;", " ") : expectedCard.getDescription();
            String actualDescription = found.get().getDescription() != null ? found.get().getDescription().replace("&nbsp;", " ") : found.get().getDescription();
            assertEquals(expectedCard.getName() + " description error", expectedDescription, actualDescription);
        });
    }
}
