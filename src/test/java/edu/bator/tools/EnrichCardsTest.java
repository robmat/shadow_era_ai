package edu.bator.tools;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bator.cards.Ally.Affinity;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;

public class EnrichCardsTest {

  private static final Logger log = Logger.getLogger(EnrichCardsTest.class);

  @Test
  @Ignore
  public void test() throws IOException {
    List<Map<String, Object>> presentCards = new ObjectMapper()
        .readValue(getClass().getResourceAsStream("/cards.json"), new TypeReference<>() {});

    Document cardDoc = Jsoup.parse(new URL("http://www.shadowera.com/cards.php"), 10000);
    Elements allRows = cardDoc.getElementsByTag("tr");
    for (Element row : allRows) {
      if (!row.children().isEmpty()) {
        String code = row.children().get(0).wholeText().trim();
        Optional<Map<String, Object>> card = presentCards.stream().filter(c -> Objects.equals(c.get("code"), code)).findAny();
        if (card.isPresent() && row.children().size() >= 5) {
          log.info(row);
          log.info(card);
          try {
            log.info(Affinity.valueOf(row.children().get(6).wholeText().trim().toUpperCase()));
          } catch (IllegalArgumentException e) {
            log.warn(e, e);
            continue;
          }
          card.get().put("affinity", Affinity.valueOf(row.children().get(6).wholeText().trim().toUpperCase()));
        }
      }
    }

    Files.write(Paths.get("src", "main", "resources", "enriched_cards.json"), new ObjectMapper().writeValueAsBytes(presentCards));
  }
}