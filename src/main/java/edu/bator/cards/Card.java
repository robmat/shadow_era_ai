package edu.bator.cards;

import java.util.HashSet;
import java.util.Set;

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

    ItemSubType itemSubType;

    AttackType attackType;

    Set<Ability> abilities = new HashSet<>();

    public Card(
            String code,
            String rarity,
            String name,
            Side side,
            HeroClass heroClass,
            String description,
            CardType cardType,
            Integer resourceCost,
            Integer attack,
            Integer initialHp,
            ItemSubType itemSubType,
            AttackType attackType,
            Set<Ability> abilities) {
        this.code = code;
        this.rarity = rarity;
        this.name = name;
        this.side = side;
        this.heroClass = heroClass;
        this.description = description;
        this.cardType = cardType;
        this.resourceCost = resourceCost;
        this.attack = attack;
        this.initialHp = initialHp;
        this.itemSubType = itemSubType;
        this.attackType = attackType;
        this.abilities = new HashSet<>(abilities);
    }
}
