package edu.bator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import edu.bator.cards.AllCardsSet;
import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.cards.Support;
import edu.bator.cards.enums.CardEnums;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AllDoneSupportsShouldHaveTypeSupportTest {

    @Test
    public void shouldTest() throws IOException, ClassNotFoundException {
        AllCardsSet allCardsSet = new AllCardsSet();
        List<Path> files = Files.list(Paths.get("src", "main", "java", "edu", "bator", "cards", "done")).collect(Collectors.toList());
        for (Card card : allCardsSet.getAllCards()) {
            for (Path file : files) {
                Class<?> clazz = Class.forName(file.toString().replace("src\\main\\java\\", "").replace(".java", "").replace("\\", "."));
                if (card instanceof Support && clazz.getSimpleName().equals(card.getName().replaceAll("[ :'!,-]", ""))) {
                    assertEquals(card.getName() + " should of support type.", card.getCardType(), CardEnums.CardType.SUPPORT);
                }
            }
        }
    }
}
