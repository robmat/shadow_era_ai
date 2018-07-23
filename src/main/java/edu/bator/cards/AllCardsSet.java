package edu.bator.cards;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
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

    LinkedList<Card> allCards = new LinkedList<>();

    public AllCardsSet() {
        readCards();
    }

    private void readCards() {
        try {
            LinkedList<Card> cardsList = new LinkedList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            allCards = objectMapper.readValue(new File(this.getClass().getResource("/cards.json").toURI()), new TypeReference<LinkedList<Card>>() {
            });
            for (Card card : allCards) {
                Card replaceCard = replaceWithImplementingCard(card);
                cardsList.add(replaceCard);
            }
            allCards = cardsList;
        } catch (Exception e) {
            log.error("Cards not read:", e);
            System.exit(1);
        }
    }

    @SuppressWarnings("unchecked")
    public Card replaceWithImplementingCard(Card card) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = "edu.bator.cards.todo." + card.getName().replaceAll("[ :'!,-]", "");
        Class<? extends Card> clazz = null;
        try {
            clazz = (Class<? extends Card>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            clazz = (Class<? extends Card>) Class.forName(className.replaceAll("todo", "done"));
        }
        Constructor<? extends Card> constructor = clazz.getConstructor(Card.class);
        return constructor.newInstance(card);
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
