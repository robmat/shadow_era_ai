package edu.bator;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bator.cards.Card;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class CardParser {

    private static List<String> ALLIES_WITH_CLAW = Arrays.asList(new String[] {
            "Spark", "Pack Wolf", "Ogloth the Glutton", "Wulven Tracker",
            "Molten Destroyer", "Cobra Demon", "Carniboar", "Hellsteed",
            "Medusil", "Bad Wolf", "Fire Snake", "Chimera", "Plasma Behemoth",
            "Brutalis", "Infernal Gargoyle", "Earthen Protector", "Marshland Sentinel",
            "Armored Sandworm"
    });

    private static final Logger log = Logger.getLogger(CardParser.class);

    public static void main(String[] args) throws Exception {
        List<Card> cards = new LinkedList<>();

        Document cardDoc = Jsoup.parse(new URL("http://www.shadowera.com/cards.php"), 10000);
        //log.info(cardDoc);
        Elements allRows = cardDoc.getElementsByTag("tr");
        for (Element row : allRows) {
            log.info(row);
            if (row.childNodeSize() > 0 &&
                    nonNull(row.childNode(0)) &&
                    row.childNode(0).childNodeSize() > 0 &&
                    "td".equalsIgnoreCase(row.childNode(0).nodeName()) &&
                    row.childNode(0).childNode(0) instanceof TextNode &&
                    ((TextNode) row.childNode(0).childNode(0)).text().matches("[a-z][a-z]\\d\\d\\d")) {
                boolean longerTableRow = row.childNodeSize() == 13;
                Card card = new Card();

                card.setCode(((TextNode) row.childNode(0).childNode(0)).text());

                if (row.childNode(1).childNodeSize() > 0) {
                    card.setRarity(row.childNode(1).childNode(0).childNode(0).toString());
                }

                if ("H".equalsIgnoreCase(card.getRarity())) {
                    card.setCardType(Card.CardType.HERO);
                }

                card.setName(row.childNode(2).childNode(0).childNode(0).toString());


                if (row.childNode(3).childNodeSize() > 0) {
                    String typeFactionString = row.childNode(3).childNode(0).toString();
                    if (StringUtils.containsIgnoreCase(typeFactionString, "human")) {
                        card.setSide(Card.Side.HUMAN);
                    }
                    if (StringUtils.containsIgnoreCase(typeFactionString, "shadow")) {
                        card.setSide(Card.Side.SHADOW);
                    }

                    card.setHeroType(Card.HeroType.parse(typeFactionString));
                    card.setItemSubType(Card.ItemSubType.parse(typeFactionString));

                    if (Card.ItemSubType.WEAPON.equals(card.getItemSubType())) {
                        card.setCardType(Card.CardType.ITEM);
                    }

                    if (isNull(card.getCardType())) {
                        if (Objects.equals("Hero Weapon", typeFactionString)) {
                            card.setCardType(Card.CardType.ITEM);
                            card.setItemSubType(Card.ItemSubType.WEAPON);
                            if (nonNull(typeFactionString) && typeFactionString.contains("Bow")) {
                                card.setAttackType(Card.AttackType.BOW);
                            } else if ("Fangs of the Predator".equals(card.getName()) || "What Big Teeth".equals(card.getName())) {
                                card.setAttackType(Card.AttackType.CLAW);
                            } else {
                                card.setAttackType(Card.AttackType.SWORD);
                            }
                        } else if (Objects.equals("Hero Armor", typeFactionString)) {
                            card.setCardType(Card.CardType.ITEM);
                            card.setItemSubType(Card.ItemSubType.ARMOR);
                        } else {
                            card.setCardType(Card.CardType.parse(typeFactionString));
                        }
                    }
                }

                if (longerTableRow) {
                    card.setCardType(Card.CardType.parse(row.childNode(5).childNode(0).toString()));
                }

                if (longerTableRow) {
                    card.setItemSubType(row.childNode(6).childNodeSize() > 0 ? Card.ItemSubType.parse(row.childNode(6).childNode(0).toString()) : null);
                }

                if (!longerTableRow) {
                    card.setResourceCost(parseIntInChildIfPresent(row.childNode(4)));
                } else {
                    card.setResourceCost(parseIntInChildIfPresent(row.childNode(9)));
                }

                if (!longerTableRow) {
                    card.setAttack(parseIntInChildIfPresent(row.childNode(5)));
                } else {
                    card.setAttack(parseIntInChildIfPresent(row.childNode(10)));
                }

                if (!longerTableRow) {
                    card.setInitialHp(parseIntInChildIfPresent(row.childNode(6)));
                } else {
                    card.setInitialHp(parseIntInChildIfPresent(row.childNode(11)));
                }

                if (!longerTableRow) {
                    card.setDescription(row.childNode(7).childNodeSize() > 0 ? row.childNode(7).childNode(0).toString() : null);
                } else {
                    card.setDescription(row.childNode(12).childNodeSize() > 0 ? row.childNode(12).childNode(0).toString() : null);
                }

                if (longerTableRow) {
                    card.setAttackType(row.childNode(7).childNodeSize() > 0 ? Card.AttackType.parse(row.childNode(7).childNode(0).toString()) : null);
                } else {
                    if (card.getCardType().equals(Card.CardType.ALLY)) {
                        if (ALLIES_WITH_CLAW.contains(card.getName())) {
                            card.setAttackType(Card.AttackType.CLAW);
                        } else {
                            card.setAttackType(Card.AttackType.SWORD);
                        }
                    }
                }

                cards.add(card);
            }
        }
        cards.stream()
                .filter(card -> nonNull(card.getAttack()) && isNull(card.getAttackType()))
                .forEach(log::info);

        Path resultPath = Paths.get("result.txt");
        Files.write(resultPath, cards.stream().map(Card::toString).collect(Collectors.toList()));
        Files.write(Paths.get("src", "main", "resources", "cards.json"), new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsBytes(cards));
    }

    public static Integer parseIntInChildIfPresent(Node node) {
        try {
            return Integer.valueOf(node.childNode(0).toString());
        } catch (Exception e) {
            return null;
        }
    }
}
