package edu.bator.game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import edu.bator.cards.AllCardsSet;
import edu.bator.cards.Card;
import edu.bator.ui.GamePainter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameState {

    private static final Logger log = Logger.getLogger(GameState.class);

    int currentTurn = 0;

    Card enemyHero;
    Card yourHero;

    LinkedList<Card> enemyDeck = new LinkedList<>();
    LinkedList<Card> yourDeck = new LinkedList<>();

    LinkedList<Card> enemyHand = new LinkedList<>();
    LinkedList<Card> yourHand = new LinkedList<>();

    LinkedList<Card> enemyGraveyard = new GraveyardLinkedList();
    LinkedList<Card> yourGraveyard = new GraveyardLinkedList();

    LinkedList<Card> enemyResources = new LinkedList<>();
    LinkedList<Card> yourResources = new LinkedList<>();

    Integer yourCurrentResources, enemyCurrentResources = 0;

    LinkedList<Card> enemyAllies = new LinkedList<>();
    LinkedList<Card> yourAllies = new LinkedList<>();

    LinkedList<Card> enemySupport = new LinkedList<>();
    LinkedList<Card> yourSupport = new LinkedList<>();

    AllCardsSet allCardsSet = new AllCardsSet();

    GamePhase gamePhase = GamePhase.YOU_PREPARE;

    @JsonIgnore
    private GamePainter gamePainter;

    private Card attackSource;
    private Card abilitySource;

    public void init() {
        enemyHero = allCardsSet.cloneByName("Boris Skullcrusher");
        yourHero = allCardsSet.cloneByName("Boris Skullcrusher");

        for (int i = 0; i < 40; i++) {
            enemyDeck.add(allCardsSet.cloneByName("Kristoffer Wyld"));
            yourDeck.add(allCardsSet.cloneByName("Kristoffer Wyld"));
        }

        enemyHand.add(allCardsSet.cloneByName("Lily Rosecult"));
        yourHand.add(allCardsSet.cloneByName("Lily Rosecult"));

        enemyHand.add(allCardsSet.cloneByName("Jasmine Rosecult"));
        yourHand.add(allCardsSet.cloneByName("Jasmine Rosecult"));

        enemyHand.add(allCardsSet.cloneByName("Sandra Trueblade"));
        yourHand.add(allCardsSet.cloneByName("Sandra Trueblade"));

        log.info("Init done.");
    }

    public boolean cardIsInHand(Card card) {
        return (Arrays.asList(GamePhase.YOU_ACTION, GamePhase.YOU_SACRIFICE).contains(gamePhase)
                && yourHand.stream()
                .anyMatch(inHand -> Objects.equal(card, inHand))) ||
                (Arrays.asList(GamePhase.ENEMY_ACTION, GamePhase.ENEMY_SACRIFICE).contains(gamePhase)
                        && enemyHand.stream()
                        .anyMatch(inHand -> Objects.equal(card, inHand)));
    }

    public boolean cardIsInAllies(Card card) {
        return (java.util.Objects.equals(GamePhase.YOU_ACTION, gamePhase) && yourAllies.stream()
                .anyMatch(inHand -> Objects.equal(card, inHand))) ||
                (java.util.Objects.equals(GamePhase.ENEMY_ACTION, gamePhase) && enemyAllies.stream()
                        .anyMatch(inHand -> Objects.equal(card, inHand)));
    }

    public void repaint() {
        if (java.util.Objects.nonNull(gamePainter)) {
            gamePainter.paint(this);
        }
    }

    public void resetPossibleAttackTargets() {
        heroesAlliesAndSupportCards().forEach(card -> card.setPossibleAttackTarget(false));
    }

    private LinkedList<Card> heroesAlliesAndSupportCards() {
        LinkedList<Card> cardList = Stream.concat(allAllies().stream(), allSupports().stream())
                .collect(Collectors.toCollection(LinkedList::new));
        cardList.add(enemyHero);
        cardList.add(yourHero);
        return cardList;
    }

    private LinkedList<Card> allAllies() {
        return Stream.concat(enemyAllies.stream(), yourAllies.stream())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private LinkedList<Card> allSupports() {
        return Stream.concat(enemySupport.stream(), yourSupport.stream())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    void increaseSE(Card hero) {
        hero.setShadowEnergy(hero.getShadowEnergy() + 1);
    }

    public LinkedList<Card> enemyHeroAlliesAndSupportCards() {
        LinkedList<Card> cards = new LinkedList<>();
        cards.add(enemyHero);
        cards.addAll(enemyAllies);
        cards.addAll(enemySupport);
        return cards;
    }

    public LinkedList<Card> yourHeroAlliesAndSupportCards() {
        LinkedList<Card> cards = new LinkedList<>();
        cards.add(yourHero);
        cards.addAll(yourAllies);
        cards.addAll(yourSupport);
        return cards;
    }

    public void resetPossibleAbiltyTargets() {
        heroesAlliesAndSupportCards().forEach(card -> card.setPossibleAbilityTarget(false));
    }

    List<Card> allCardsInPlay() {
        return Stream.concat(enemyHeroAlliesAndSupportCards().stream(), yourHeroAlliesAndSupportCards().stream()).collect(Collectors.toList());
    }

    public int enemyResourcesSize() {
        return enemyResources.size();
    }

    public int yourResourcesSize() {
        return yourResources.size();
    }

    public List<Card> currentEnemyHandBasedOnPhase() {
        if (GamePhase.YOU_ACTION.equals(gamePhase)) return getEnemyHand();
        if (GamePhase.ENEMY_ACTION.equals(gamePhase)) return getYourHand();
        return null;
    }
}
