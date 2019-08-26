package edu.bator.cards;

import edu.bator.EntryPoint;
import edu.bator.cards.effects.Effect;
import edu.bator.cards.enums.Owner;
import edu.bator.game.GameEngine;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.BiConsumer;

import static edu.bator.cards.enums.CardEnums.Ability;
import static edu.bator.cards.enums.CardEnums.*;
import static java.util.Objects.nonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card implements Cloneable {

    String code;
    String rarity;
    String name;
    Side side;
    Set<HeroClass> heroClass;

    String description;

    CardType cardType;

    Integer resourceCost;
    Integer baseAttack;
    Integer baseDefence;
    Integer initialHp;
    Integer currentHp;
    Integer shadowEnergy = 0;

    ItemSubType itemSubType;

    AttackType attackType;

    Set<Ability> abilities = new HashSet<>();

    boolean attackReadied, castable, possibleAttackTarget, possibleAbilityTarget, abilityReadied, unique = false;

    LinkedList<Effect> effects = new LinkedList<>();

    Weapon weapon;
    Armor armor;

    Set<HeroClass> availableForHeroClasses = new HashSet<>();

    Owner owner;

    Set<Attachment> attachments = new HashSet<>();

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
        this.baseDefence = cloneFrom.baseDefence;
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
                this.weapon = (Weapon) cloneFrom.weapon.clone();
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        try {
            if (nonNull(cloneFrom.armor)) {
                this.armor = (Armor) cloneFrom.armor.clone();
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        this.availableForHeroClasses = new HashSet<>(cloneFrom.availableForHeroClasses);
        this.unique = cloneFrom.unique;
        this.owner = cloneFrom.owner;
        this.attachments = new HashSet<>(cloneFrom.attachments);
    }

    public void tryToReadyAttack(GameState gameState) {
        attackReadied = gameState.allCardsInPlay().stream().noneMatch(card -> card.preventsAllyFromReadyingAttack(this, gameState));
    }

    public boolean preventsAllyFromReadyingAttack(Card card, GameState gameState) {
        return false;
    }

    public void tryToReadyAbility(GameState gameState) {
        abilityReadied = gameState.allCardsInPlay().stream().noneMatch(card -> card.preventsAllyFromReadyingAbility(this, gameState));
    }

    private boolean preventsAllyFromReadyingAbility(Card card, GameState gameState) {
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Card clone = (Card) super.clone();
        clone.setAbilities(new HashSet<>(abilities));
        clone.setUniqueId(UUID.randomUUID().toString());
        clone.setEffects(new LinkedList<>(effects));
        clone.setAvailableForHeroClasses(new HashSet<>(availableForHeroClasses));
        clone.setAttachments(new HashSet<>(attachments));
        return clone;
    }

    public void determineCastable(GameState gameState) {
        boolean you = gameState.getGamePhase().equals(GamePhase.YOU_ACTION)
                && gameState.getYourCurrentResources() >= getResourceCost();
        boolean enemy = gameState.getGamePhase().equals(GamePhase.ENEMY_ACTION)
                && gameState.getEnemyCurrentResources() >= getResourceCost();
        this.castable = you || enemy;
    }

    public void wasCasted(GameState gameState) {
        if (abilities.contains(Ability.HASTE) && cardIsAnAlly()) {
            setAttackReadied(true);
            setAbilityReadied(true);
        }
    }

    public void calculatePossibleAttackTarget(Card attackSource, GameState gameState) {
        boolean isNotStealth = !getAbilities(gameState).contains(Ability.STEALTH);
        boolean possibleAllyTarget = calculatePossibleTargetProtectorIncluded(gameState) && isNotStealth;
        boolean askOtherCards = askOtherCards(attackSource, gameState);

        if (cardIsAHero()) {
            setPossibleAttackTarget(true);
        } else {
            setPossibleAttackTarget(possibleAllyTarget && askOtherCards);
        }
    }

    private boolean askOtherCards(Card attackSource, GameState gameState) {
        return gameState.allCardsInPlay().stream()
                .map(card -> card.influenceAttackTargetPossible(this, attackSource, gameState))
                .reduce(true, (one, two) -> one && two);
    }

    protected boolean influenceAttackTargetPossible(Card target, Card attackSource, GameState gameState) {
        return true;
    }

    private boolean calculatePossibleTargetProtectorIncluded(GameState gameState) {
        return calculatePossibleEnemyTargetProtectorIncluded(this, gameState);
    }

    public boolean calculatePossibleEnemyTargetProtectorIncluded(Card card, GameState gameState) {
        List<Card> enemyAllies = gameState.currentEnemyHeroAndAlliesBasedOnPhase();
        return calculatePossibleTargetProtectorIncluded(card, gameState, enemyAllies);
    }

    public boolean calculatePossibleAllyTargetProtectorIncluded(Card card, GameState gameState) {
        List<Card> yourAllies = gameState.currentYourAlliesBasedOnPhase();
        return calculatePossibleTargetProtectorIncluded(card, gameState, yourAllies);
    }

    private boolean calculatePossibleTargetProtectorIncluded(Card card, GameState gameState, List<Card> allies) {
        boolean otherAllyHasProtector = allies
                .stream()
                .filter(card1 -> !Objects.equals(card1, this))
                .anyMatch(card1 -> card1.getAbilities(gameState).contains(Ability.PROTECTOR));
        boolean hasProtector = card.getAbilities(gameState).contains(Ability.PROTECTOR);
        boolean isAllyWithProtector = card.cardIsAnAlly() && hasProtector;
        boolean isAllyAndNoOtherHasProtector = card.cardIsAnAlly() && !otherAllyHasProtector;

        return (isAllyWithProtector || isAllyAndNoOtherHasProtector) && allies.contains(card);
    }

    public boolean cardIsAnAlly() {
        return CardType.ALLY.equals(cardType);
    }

    public boolean cardIsAnAbility() {
        return CardType.ABILITY.equals(cardType);
    }

    public boolean cardIsSupport() {
        return CardType.SUPPORT.equals(cardType);
    }

    public boolean cardIsAHero() {
        return CardType.HERO.equals(cardType);
    }

    public boolean cardIsAWeapon() {
        return ItemSubType.WEAPON.equals(getItemSubType());
    }

    public boolean cardIsAArmor() {
        return ItemSubType.ARMOR.equals(getItemSubType());
    }

    public boolean cardIsArtifact() {
        return ItemSubType.ARTIFACT.equals(getItemSubType());
    }

    public boolean cardIsAttachment() {
        return ItemSubType.ATTACHMENT.equals(getItemSubType());
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
            weapon.setCurrentHp(weapon.getCurrentHp() - 1 + weapon.getAttachments().stream().map(Attachment::durabilityInCombatLost).reduce(0, (a, b) -> a + b));
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
        new GameEngine().determineCurrentHandCardsCastable(gameState);
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

    public void cardHasDiedEvent(Card card, GameState gameState) {
    }

    public Integer getAttack(GameState gameState) {
        return baseAttack;
    }

    public Integer modifiesAttack(Card card, GameState gameState) {
        return 0;
    }

    public Integer getCurrentHp(GameState gameState) {
        return Optional.ofNullable(currentHp).orElse(initialHp);
    }

    public Integer currentHpWithoutBonus() {
        return Optional.ofNullable(currentHp).orElse(initialHp);
    }

    public Integer modifiesHp(Card card, GameState gameState) {
        return 0;
    }

    public void resetAttackAnHp() {
        currentHp = initialHp;
        baseAttack = EntryPoint.allCardsSet.cloneByName(name, Owner.ENEMY).getBaseAttack();
    }

    public void supportIsCast(GameState gameState) {
    }

    public void gamePhaseChangeEvent(GameState gameState) {
    }

    public void artifactIsCast(GameState gameState) {
    }

    public void abilityClickEvent(GameState gameState) {
    }

    public Set<Ability> getAbilities(GameState gameState) {
        Set<Ability> abilities = new HashSet<>(this.abilities);
        gameState.allCardsInPlay().forEach(card -> card.modifiesAbilities(abilities, this, gameState));
        return abilities;
    }

    public void modifiesAbilities(Set<Ability> abilities, Card card, GameState gameState) {
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
                "baseAttack=" + baseAttack +
                ", initialHp=" + initialHp +
                ", currentHp=" + currentHp +
                ", owner=" + owner +
                ", uniqueId='" + uniqueId + '\'' +
                '}';
    }
}
