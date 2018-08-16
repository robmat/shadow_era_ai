package edu.bator.cards;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import edu.bator.cards.effects.Effect;
import edu.bator.cards.enums.CardEnums;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static edu.bator.cards.enums.CardEnums.Ability;
import static edu.bator.cards.enums.CardEnums.AttackType;
import static edu.bator.cards.enums.CardEnums.CardType;
import static edu.bator.cards.enums.CardEnums.HeroClass;
import static edu.bator.cards.enums.CardEnums.ItemSubType;
import static edu.bator.cards.enums.CardEnums.Side;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card implements Cloneable {

    String code;
    String rarity;
    String name;
    Side side;
    HeroClass heroClass;

    String description;

    CardType cardType;

    Integer resourceCost;
    Integer attack;
    Integer initialHp;
    Integer currentHp;
    Integer shadowEnergy = 0;

    ItemSubType itemSubType;

    AttackType attackType;

    Set<Ability> abilities = new HashSet<>();

    boolean attackReadied, castable, possibleAttackTarget, possibleAbilityTarget, abilityReadied = false;

    LinkedList<Effect> effects = new LinkedList<>();

    String uniqueId = UUID.randomUUID().toString();

    public Card(Card cloneFrom) {
        this.code = cloneFrom.code;
        this.rarity = cloneFrom.rarity;
        this.name = cloneFrom.name;
        this.side = cloneFrom.side;
        this.heroClass = cloneFrom.heroClass;
        this.description = cloneFrom.description;
        this.cardType = cloneFrom.cardType;
        this.resourceCost = cloneFrom.resourceCost;
        this.attack = cloneFrom.attack;
        this.initialHp = cloneFrom.initialHp;
        this.currentHp = cloneFrom.currentHp;
        this.itemSubType = cloneFrom.itemSubType;
        this.attackType = cloneFrom.attackType;
        this.abilities = new HashSet<>(cloneFrom.abilities);
        this.attackReadied = cloneFrom.attackReadied;
        this.castable = cloneFrom.castable;
        this.possibleAttackTarget = cloneFrom.possibleAttackTarget;
        this.uniqueId = UUID.randomUUID().toString();
        this.shadowEnergy = cloneFrom.shadowEnergy;
        this.possibleAbilityTarget = cloneFrom.possibleAbilityTarget;
        this.abilityReadied = cloneFrom.abilityReadied;
        this.effects = new LinkedList<>(cloneFrom.effects);
    }

    public void tryToReady() {
        attackReadied = true;
    }

    public void tryToReadyAbility() {
        abilityReadied = true;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Card clone = (Card) super.clone();
        clone.setAbilities(new HashSet<>(abilities));
        clone.setUniqueId(UUID.randomUUID().toString());
        return clone;
    }

    public void determineCastable(Card card, GameState gameState) {
        boolean you = gameState.getGamePhase().equals(GamePhase.YOU_ACTION)
                && gameState.getYourCurrentResources() >= card.getResourceCost();
        boolean enemy = gameState.getGamePhase().equals(GamePhase.ENEMY_ACTION)
                && gameState.getEnemyCurrentResources() >= card.getResourceCost();
        this.castable = you || enemy;
    }

    public void wasCasted(GameState gameState) {
        if (abilities.contains(Ability.HASTE)) {
            setAttackReadied(true);
            setAbilityReadied(true);
        }
    }

    public void calculatePossibleAttackTarget(Card attackSource, GameState gameState) {
        boolean possibleAllyTarget = calculatePossibleAllyTarget(gameState);

        if (cardIsAHero()) {
            setPossibleAttackTarget(true);
        } else {
            setPossibleAttackTarget(possibleAllyTarget);
        }
    }

    private boolean calculatePossibleAllyTarget(GameState gameState) {
        boolean otherAllyHasProtector = gameState.currentEnemyHeroAndAlliesBasedOnPhase()
                .stream()
                .filter(card -> !Objects.equals(card, this))
                .anyMatch(card -> card.getAbilities().contains(Ability.PROTECTOR));
        boolean hasProtector = getAbilities().contains(Ability.PROTECTOR);
        boolean isAllyWithProtector = cardIsAnAlly() && hasProtector;
        boolean isAllyAndNoOtherHasProtector = cardIsAnAlly() && !otherAllyHasProtector;
        return isAllyWithProtector || isAllyAndNoOtherHasProtector;
    }

    public boolean cardIsAnAlly() {
        return CardType.ALLY.equals(cardType);
    }

    public boolean cardIsAHero() {
        return CardType.HERO.equals(cardType);
    }

    public boolean cardIsDead() {
        return Objects.nonNull(getCurrentHp()) && getCurrentHp() <= 0;
    }

    public void attackedBy(GameState gameState, Card attackSource) {
        if (Objects.nonNull(attackSource.getAttack()) && Objects.nonNull(getCurrentHp())) {
            setCurrentHp(getCurrentHp() - attackSource.getAttack());
        }
        if (!cardIsDead() && !attackSource.getAbilities().contains(Ability.AMBUSH)) {
            if (Objects.nonNull(attackSource.getCurrentHp()) && Objects.nonNull(getAttack())) {
                attackSource.setCurrentHp(attackSource.getCurrentHp() - getAttack());
            }
        }
    }

    public boolean hasAbilityToUse(GameState gameState) {
        return false;
    }

    public void calculatePossibleAbilityTarget(Card abilitySource, GameState gameState) {
        boolean possibleAllyTarget = calculatePossibleAllyTarget(gameState);

        if (possibleAllyTarget) {
            this.possibleAbilityTarget = abilitySource.ableToApplyAbilityTo(this);
        }
    }

    public boolean ableToApplyAbilityTo(Card card) {
        return false;
    }

    public void applyAbility(Card card, GameState gameState) {
    }

    public void resetFlags() {
        setPossibleAttackTarget(false);
        setPossibleAbilityTarget(false);
        setAbilityReadied(false);
        setAttackReadied(false);
        setCastable(false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(getUniqueId(), card.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }

    public boolean canAttack() {
        return isAttackReadied() && getAttack() > 0;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "currentHp=" + currentHp +
                ", uniqueId='" + uniqueId + '\'' +
                '}';
    }
}
