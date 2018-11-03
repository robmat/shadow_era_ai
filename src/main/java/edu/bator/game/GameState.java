package edu.bator.game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import edu.bator.cards.Armor;
import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.cards.Weapon;
import edu.bator.cards.enums.Owner;
import edu.bator.ui.GamePainter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import static edu.bator.EntryPoint.allCardsSet;

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

    GraveyardLinkedList enemyGraveyard = new GraveyardLinkedList();
    GraveyardLinkedList yourGraveyard = new GraveyardLinkedList();

    LinkedList<Card> enemyResources = new LinkedList<>();
    LinkedList<Card> yourResources = new LinkedList<>();

    Integer yourCurrentResources, enemyCurrentResources = 0;

    LinkedList<Card> enemyAllies = new LinkedList<>();
    LinkedList<Card> yourAllies = new LinkedList<>();

    LinkedList<Card> enemySupport = new LinkedList<>();
    LinkedList<Card> yourSupport = new LinkedList<>();

    GamePhase gamePhase = GamePhase.YOU_PREPARE;

    @JsonIgnore
    private GamePainter gamePainter;

    private Card attackSource;
    private Card abilitySource;

    public void init() {
        enemyHero = allCardsSet.cloneByName("Boris Skullcrusher", Owner.ENEMY);
        enemyHero.setWeapon((Weapon) allCardsSet.cloneByName("Mournblade", Owner.ENEMY));
        enemyHero.setArmor((Armor) allCardsSet.cloneByName("Snow Sapphire", Owner.ENEMY));

        yourHero = allCardsSet.cloneByName("Boris Skullcrusher", Owner.YOU);
        yourHero.setWeapon((Weapon) allCardsSet.cloneByName("Mournblade", Owner.YOU));
        yourHero.setArmor((Armor) allCardsSet.cloneByName("Snow Sapphire", Owner.YOU));

        for (int i = 0; i < 40; i++) {
            enemyDeck.add(allCardsSet.cloneByName("Birgitte Skullborn", Owner.ENEMY));
            yourDeck.add(allCardsSet.cloneByName("Birgitte Skullborn", Owner.YOU));
        }

        enemyHand.add(allCardsSet.cloneByName("Snow Sapphire", Owner.ENEMY));
        yourHand.add(allCardsSet.cloneByName("Snow Sapphire", Owner.YOU));

        enemyHand.add(allCardsSet.cloneByName("Warrior Training", Owner.ENEMY));
        yourHand.add(allCardsSet.cloneByName("Warrior Training", Owner.YOU));

        enemyHand.add(allCardsSet.cloneByName("Warrior Training", Owner.ENEMY));
        yourHand.add(allCardsSet.cloneByName("Warrior Training", Owner.YOU));

        enemyHand.add(allCardsSet.cloneByName("Crippling Blow", Owner.ENEMY));
        yourHand.add(allCardsSet.cloneByName("Crippling Blow", Owner.YOU));

        enemyHand.add(allCardsSet.cloneByName("Enrage", Owner.ENEMY));
        yourHand.add(allCardsSet.cloneByName("Enrage", Owner.YOU));

        enemyHand.add(allCardsSet.cloneByName("Jasmine Rosecult", Owner.ENEMY));
        yourHand.add(allCardsSet.cloneByName("Jasmine Rosecult", Owner.YOU));

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

    public boolean cardIsInCurrentAllies(Card card) {
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
        allCardsInPlay().forEach(card -> card.setPossibleAttackTarget(false));
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
        allCardsInPlay().forEach(card -> card.setPossibleAbilityTarget(false));
    }

    public List<Card> allCardsInPlay() {
        Set<Attachment> attachments = Stream.concat(enemyHeroAlliesAndSupportCards().stream(), yourHeroAlliesAndSupportCards().stream())
                .map(Card::getAttachments)
                .reduce((a, b) -> new HashSet<>() {{
                    addAll(a);
                    addAll(b);
                }}).orElse(new HashSet<>());
        return Stream.concat(Stream.concat(enemyHeroAlliesAndSupportCards().stream(), yourHeroAlliesAndSupportCards().stream()), attachments.stream())
                .distinct()
                .collect(Collectors.toList());
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
        return new LinkedList<>();
    }

    public List<Card> currentYourHandBasedOnPhase() {
        if (GamePhase.YOU_ACTION.equals(gamePhase)) return getYourHand();
        if (GamePhase.ENEMY_ACTION.equals(gamePhase)) return getEnemyHand();
        return new LinkedList<>();
    }

    public List<Card> currentEnemyHeroAndAlliesBasedOnPhase() {
        List<Card> cards = new LinkedList<>();
        if (GamePhase.YOU_ACTION.equals(gamePhase)) {
            cards.add(enemyHero);
            cards.addAll(enemyAllies);
        }
        if (GamePhase.ENEMY_ACTION.equals(gamePhase)) {
            cards.add(yourHero);
            cards.addAll(yourAllies);
        }
        return cards;
    }

    public boolean cardIsCurrentHero(Card card) {
        if (GamePhase.YOU_ACTION.equals(gamePhase)) {
            return card.equals(getYourHero());
        }
        if (GamePhase.ENEMY_ACTION.equals(gamePhase)) {
            return card.equals(getEnemyHero());
        }
        return false;
    }

    public List<Card> currentEnemyAlliesBasedOnPhase() {
        if (GamePhase.YOU_ACTION.equals(gamePhase)) {
            return enemyAllies;
        }
        if (GamePhase.ENEMY_ACTION.equals(gamePhase)) {
            return yourAllies;
        }
        return new LinkedList<>();
    }

    public List<Card> currentYourAlliesBasedOnPhase() {
        if (GamePhase.YOU_ACTION.equals(gamePhase)) {
            return yourAllies;
        }
        if (GamePhase.ENEMY_ACTION.equals(gamePhase)) {
            return enemyAllies;
        }
        return new LinkedList<>();
    }

    public Card yourHeroBasedOnPhase() {
        if (GamePhase.YOU_ACTION.equals(gamePhase)) {
            return yourHero;
        }
        if (GamePhase.ENEMY_ACTION.equals(gamePhase)) {
            return enemyHero;
        }
        return null;
    }

    public Card enemyHeroBasedOnPhase() {
        return GamePhase.YOU_ACTION.equals(getGamePhase()) ? enemyHero : (GamePhase.ENEMY_ACTION.equals(getGamePhase())) ? yourHero : null;
    }

    public boolean yourAction() {
        return GamePhase.YOU_ACTION.equals(getGamePhase());
    }

    public boolean enemyAction() {
        return GamePhase.ENEMY_ACTION.equals(getGamePhase());
    }
}
