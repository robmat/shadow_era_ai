package edu.bator.cards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.log4j.Logger;

@Data
@AllArgsConstructor
@Builder
public class AllCardsSet {

    private static final Logger log = Logger.getLogger(AllCardsSet.class);

    List<Card> allCards = new ArrayList<>();

    public AllCardsSet() {
        readCards();
    }

    private void readCards() {
        try {
            allCards = new ObjectMapper().readValue(new File(this.getClass().getResource("/cards.json").toURI()), new TypeReference<List<Card>>() {});
        } catch (Exception e) {
            log.error("Cards not read:", e);
            System.exit(1);
        }
    }

    public Card cloneByName(String name) {
        Card found = allCards
                .stream()
                .filter(card -> Objects.equals(name, card.getName()))
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(found)) {
            found.setCurrentHp(found.getInitialHp());
        }
        try {
            return Objects.isNull(found) ? null : (Card) found.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Clone dead:", e);
            System.exit(1);
            return null;
        }
    }
}
