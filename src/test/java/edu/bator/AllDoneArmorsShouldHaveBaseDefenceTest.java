package edu.bator;

import static org.junit.Assert.assertNotNull;

import edu.bator.cards.AllCardsSet;
import edu.bator.cards.Card;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class AllDoneArmorsShouldHaveBaseDefenceTest {

  @Test
  public void shouldTest() throws IOException, ClassNotFoundException {
    AllCardsSet allCardsSet = new AllCardsSet();
    List<Path> files = Files.list(Paths.get("src", "main", "java", "edu", "bator", "cards", "done"))
        .collect(Collectors.toList());
    for (Card card : allCardsSet.getAllCards()) {
      for (Path file : files) {
        Class<?> clazz = Class.forName(
            file.toString().replace("src\\main\\java\\", "").replace(".java", "")
                .replace("\\", "."));
        if (card.cardIsAArmor() && clazz.getSimpleName()
            .equals(card.getName().replaceAll("[ :'!,-]", ""))) {
          assertNotNull(card.getName() + " should have base defence.", card.getBaseDefence());
        }
      }
    }
  }
}
