package edu.bator;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
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

import static edu.bator.EntryPoint.objectJsonMapper;
import static edu.bator.cards.enums.CardEnums.Ability;
import static edu.bator.cards.enums.CardEnums.AttackType;
import static edu.bator.cards.enums.CardEnums.CardType;
import static edu.bator.cards.enums.CardEnums.HeroClass;
import static edu.bator.cards.enums.CardEnums.ItemSubType;
import static edu.bator.cards.enums.CardEnums.Side;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class CardParser {

    private static final Logger log = Logger.getLogger(CardParser.class);
    private static List<String> ALLIES_WITH_CLAW = Arrays.asList(
            "Spark", "Pack Wolf", "Ogloth the Glutton", "Wulven Tracker",
            "Molten Destroyer", "Cobra Demon", "Carniboar", "Hellsteed",
            "Medusil", "Bad Wolf", "Fire Snake", "Chimera", "Plasma Behemoth",
            "Brutalis", "Infernal Gargoyle", "Earthen Protector", "Marshland Sentinel",
            "Armored Sandworm");
    private static List<String> WEAPONS_WITH_CLAW = Arrays
            .asList("What Big Teeth", "Fangs of the Predator");
    private static List<String> WEAPONS_WITH_BOW = Collections.singletonList("Beetle Demon Bow");
    private static List<String> DO_NOT_GET_ABILITIES = Arrays.asList(
            "Lance Shadowstalker", "Darkclaw", "Moonstalker",
            "Earthen Protector", "Braxnorian Soldier", "Wulven Predator",
            "Blood Moon", "Midnight Sentinel");

    public static void main(String[] args) throws Exception {
        List<Card> cards = new LinkedList<>();

        Document cardDoc = null;
        cardDoc = Jsoup.parse(new URL("http://www.shadowera.com/cards.php"), 10000);

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
                    card.setCardType(CardType.HERO);
                }

                card.setName(row.childNode(2).childNode(0).childNode(0).toString());

                if (row.childNode(3).childNodeSize() > 0) {
                    String typeFactionString = row.childNode(3).childNode(0).toString();
                    if (StringUtils.containsIgnoreCase(typeFactionString, "human")) {
                        card.setSide(Side.HUMAN);
                    }
                    if (StringUtils.containsIgnoreCase(typeFactionString, "shadow")) {
                        card.setSide(Side.SHADOW);
                    }

                    card.setHeroClass(HeroClass.parse(typeFactionString));
                    card.setItemSubType(ItemSubType.parse(typeFactionString));

                    if (ItemSubType.WEAPON.equals(card.getItemSubType())) {
                        card.setCardType(CardType.ITEM);
                        card.setAttackType(AttackType.SWORD);
                        if (WEAPONS_WITH_CLAW.contains(card.getName())) {
                            card.setAttackType(AttackType.CLAW);
                        }
                        if (WEAPONS_WITH_BOW.contains(card.getName())) {
                            card.setAttackType(AttackType.BOW);
                        }
                    }

                    if (isNull(card.getCardType())) {
                        if (Objects.equals("Hero Weapon", typeFactionString)) {
                            card.setCardType(CardType.ITEM);
                            card.setItemSubType(ItemSubType.WEAPON);
                        } else if (Objects.equals("Hero Armor", typeFactionString)) {
                            card.setCardType(CardType.ITEM);
                            card.setItemSubType(ItemSubType.ARMOR);
                        } else {
                            card.setCardType(CardType.parse(typeFactionString));
                        }
                    }
                }

                if (longerTableRow) {
                    card.setHeroClass(row.childNode(4).childNodeSize() > 0 ? HeroClass
                            .parse(row.childNode(4).childNode(0).toString()) : null);
                    card.setCardType(CardType.parse(row.childNode(5).childNode(0).toString()));
                    card.setItemSubType(row.childNode(6).childNodeSize() > 0 ? ItemSubType
                            .parse(row.childNode(6).childNode(0).toString()) : null);
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
                    card.setDescription(
                            row.childNode(7).childNodeSize() > 0 ? row.childNode(7).childNode(0).toString()
                                    : null);
                } else {
                    card.setDescription(
                            row.childNode(12).childNodeSize() > 0 ? row.childNode(12).childNode(0).toString()
                                    : null);
                }

                if (longerTableRow) {
                    card.setAttackType(row.childNode(7).childNodeSize() > 0 ? AttackType
                            .parse(row.childNode(7).childNode(0).toString()) : null);
                } else {
                    if (card.getCardType().equals(CardType.ALLY)) {
                        if (ALLIES_WITH_CLAW.contains(card.getName())) {
                            card.setAttackType(AttackType.CLAW);
                        } else {
                            card.setAttackType(AttackType.SWORD);
                        }
                    }
                }

                if (Arrays.asList(ItemSubType.ARMOR, ItemSubType.ARTIFACT, ItemSubType.TRAP)
                        .contains(card.getItemSubType()) ||
                        card.getCardType().equals(CardType.ABILITY)) {
                    card.setAttack(null);
                }

                if (card.getCardType().equals(CardType.ALLY) || ItemSubType.WEAPON
                        .equals(card.getItemSubType())) {
                    if (card.getAttackType() == null) {
                        card.setAttackType(AttackType.SWORD);
                    }
                }

                if (nonNull(card.getDescription())) {
                    for (String part : card.getDescription().split(" ")) {
                        if (Ability.parse(part) != null) {
                            if (!DO_NOT_GET_ABILITIES.contains(card.getName())) {
                                card.getAbilities().add(Ability.parse(part));
                            }
                        }
                    }
                }

                cards.add(card);
            }
        }
        cards.stream()
                .filter(card -> !card.getAbilities().isEmpty())
                .forEach(log::info);

        Path resultPath = Paths.get("result.txt");
        Files.write(resultPath, cards.stream().map(Card::toString).collect(Collectors.toList()));
        Files.write(Paths.get("src", "main", "resources", "cards.json"),
                objectJsonMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(cards));
    }

    public static Integer parseIntInChildIfPresent(Node node) {
        try {
            return Integer.valueOf(node.childNode(0).toString());
        } catch (Exception e) {
            return null;
        }
    }
}
