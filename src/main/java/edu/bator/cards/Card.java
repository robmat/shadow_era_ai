package edu.bator.cards;

import static edu.bator.cards.enums.CardEnums.Ability;
import static edu.bator.cards.enums.CardEnums.AttackType;
import static edu.bator.cards.enums.CardEnums.CardType;
import static edu.bator.cards.enums.CardEnums.HeroClass;
import static edu.bator.cards.enums.CardEnums.ItemSubType;
import static edu.bator.cards.enums.CardEnums.Side;
import static java.util.Objects.nonNull;

import edu.bator.EntryPoint;
import edu.bator.cards.effects.Effect;
import edu.bator.cards.enums.Owner;
import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    Integer baseAttack;
    Integer initialHp;
    Integer currentHp;
    Integer shadowEnergy = 0;

    ItemSubType itemSubType;

    AttackType attackType;

    Set<Ability> abilities = new HashSet<>();

    boolean attackReadied, castable, possibleAttackTarget, possibleAbilityTarget, abilityReadied, unique = false;

    LinkedList<Effect> effects = new LinkedList<>();

    Card weapon;

    Set<HeroClass> availableForHeroClasses = new HashSet<>();

    Owner owner;

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
        this.baseAttack = cloneFrom.baseAttack;
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
        try {
            if (nonNull(cloneFrom.weapon)) {
                this.weapon = (Card) cloneFrom.weapon.clone();
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        this.availableForHeroClasses = cloneFrom.availableForHeroClasses;
        this.unique = cloneFrom.unique;
    }

    public void tryToReadyAttack() {
        attackReadied = true;
    }

    public void tryToReadyAbility() {
        abilityReadied = true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Card clone = (Card) super.clone();
        clone.setAbilities(new HashSet<>(abilities));
        clone.setUniqueId(UUID.randomUUID().toString());
        clone.setEffects(new LinkedList<>(effects));
        clone.setAvailableForHeroClasses(new HashSet<>(availableForHeroClasses));
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
        boolean isNotStealth = !getAbilities().contains(Ability.STEALTH);
        boolean possibleAllyTarget = calculatePossibleTargetProtectorIncluded(gameState) && isNotStealth;

        if (cardIsAHero()) {
            setPossibleAttackTarget(true);
        } else {
            setPossibleAttackTarget(possibleAllyTarget);
        }
    }

    private boolean calculatePossibleTargetProtectorIncluded(GameState gameState) {
        return calculatePossibleTargetProtectorIncluded(this, gameState);
    }

    public boolean calculatePossibleTargetProtectorIncluded(Card card, GameState gameState) {
        boolean otherAllyHasProtector = gameState.currentEnemyHeroAndAlliesBasedOnPhase()
                .stream()
                .filter(card1 -> !Objects.equals(card1, this))
                .anyMatch(card1 -> card1.getAbilities().contains(Ability.PROTECTOR));
        boolean hasProtector = card.getAbilities().contains(Ability.PROTECTOR);
        boolean isAllyWithProtector = card.cardIsAnAlly() && hasProtector;
        boolean isAllyAndNoOtherHasProtector = card.cardIsAnAlly() && !otherAllyHasProtector;

        return isAllyWithProtector || isAllyAndNoOtherHasProtector;
    }

    public boolean cardIsAnAlly() {
        return CardType.ALLY.equals(cardType);
    }

    public boolean cardIsAnAbility() {
        return CardType.ABILITY.equals(cardType);
    }

    public boolean cardIsAHero() {
        return CardType.HERO.equals(cardType);
    }

    public boolean cardIsAWeapon() {
        return ItemSubType.WEAPON.equals(getItemSubType());
    }

    public boolean cardIsDead() {
        return nonNull(getCurrentHp()) && getCurrentHp() <= 0;
    }

    public void attackTarget(GameState gameState, Card target) {
    }

    public void attackTarget(BiConsumer<GameState, Card> attackEvent, Card target, GameState gameState) {
        target.attackedBy(attackEvent, this, gameState);
    }

    public void attackedBy(BiConsumer<GameState, Card> attackEvent, Card source, GameState gameState) {
        attackEvent.accept(gameState, this);
    }

    void reduceWeaponHp(GameState gameState, Card weapon) {
        if (nonNull(weapon.getCurrentHp())) {
            weapon.setCurrentHp(weapon.getCurrentHp() - 1);
            if (weapon.cardIsDead()) {
                new GameEngine().cardDied(weapon, gameState);
            }
        }
    }

    public boolean hasAbilityToUse(GameState gameState) {
        return false;
    }

    public void calculatePossibleAbilityTarget(Card abilitySource, GameState gameState) {
        this.possibleAbilityTarget = abilitySource.ableToApplyAbilityTo(this, gameState);
    }

    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return false;
    }

    public void applyAbility(Card target, GameState gameState) {
    }

    public void abilityAppliedToMe(BiConsumer<Card, GameState> abilityFunction, GameState gameState) {
        abilityFunction.accept(this, gameState);
    }

    public void resetFlags() {
        setPossibleAttackTarget(false);
        setPossibleAbilityTarget(false);
        setAbilityReadied(false);
        setAttackReadied(false);
        setCastable(false);
    }

    public boolean canAttack(GameState gameState) {
        return isAttackReadied() && nonNull(getAttack(gameState)) && getAttack(gameState).compareTo(0) > 0;
    }

    public Integer getAttack(GameState gameState) {
        return baseAttack;
    }

    public void cardHasDiedEvent(Card card, GameState gameState) {
    }

    public Integer modifiesAllyAttack(Ally ally, GameState gameState) {
        return 0;
    }

    public void resetAttackAnHp() {
        currentHp = initialHp;
        baseAttack = EntryPoint.allCardsSet.cloneByName(name, Owner.ENEMY).getBaseAttack();
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "currentHp=" + currentHp +
                ", uniqueId='" + uniqueId + '\'' +
                '}';
    }
}
