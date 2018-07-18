package edu.bator.cards;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    ItemSubType itemSubType;

    AttackType attackType;

    Set<Ability> abilities = new HashSet<>();

    boolean readied, castable, possibleAttackTarget = false;

    String uniqueId = UUID.randomUUID().toString();

    public void tryToReady() {
        readied = true;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Card clone = (Card) super.clone();
        clone.setAbilities(new HashSet<>(abilities));
        clone.setUniqueId(UUID.randomUUID().toString());
        return clone;
    }

    public void determineCastable(Card card, GameState gameState) {
        boolean you = gameState.getGamePhase().equals(GamePhase.YOU_ACTION) && gameState.getYourCurrentResources() >= card.getResourceCost();
        boolean enemy = gameState.getGamePhase().equals(GamePhase.ENEMY_ACTION) && gameState.getEnemyCurrentResources() >= card.getResourceCost();
        this.castable = you || enemy;
    }

    public void wasCasted(GameState gameState) {

    }

    public void calculatePossibleAttackTarget(Card attackSource) {
        setPossibleAttackTarget(cardIsAnAlly() || cardIsAHero());
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
        if (!cardIsDead()) {
            if (Objects.nonNull(attackSource.getCurrentHp()) && Objects.nonNull(getAttack())) {
                attackSource.setCurrentHp(attackSource.getCurrentHp() - getAttack());
            }
        }
    }

    public boolean hasAbility() {
        return false;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", currentHp=" + currentHp +
                ", uniqueId='" + uniqueId + '\'' +
                '}';
    }
}
