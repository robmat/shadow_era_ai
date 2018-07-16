package edu.bator.cards;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.bator.enums.EnumParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card {

    String code;
    String rarity;
    String name;
    Side side;
    HeroType heroType;

    String description;

    CardType cardType;

    Integer resourceCost;
    Integer attack;
    Integer initialHp;

    ItemSubType itemSubType;

    AttackType attackType;

    public enum AttackType {
        CLAW, SWORD, ICE, ELECTRIC, FIRE, BOW, ARCANE;

        public static AttackType parse(String str) {
            return new EnumParser<>(AttackType.class).parse(str);
        }
    }

    public enum ItemSubType {
        WEAPON, ARMOR, ARTIFACT;

        public static ItemSubType parse(String str) {
            return new EnumParser<>(ItemSubType.class).parse(str);
        }
    }

    public enum CardType {
        HERO, ALLY, ITEM, ABILITY, LOCATION;

        public static CardType parse(String str) {
            return new EnumParser<>(CardType.class).parse(str);
        }
    }

    public enum Side {
        SHADOW, HUMAN
    }

    public enum HeroType {
        WARRIOR, PRIEST, ROGUE, MAGE, HUNTER, VULVEN, ELEMENTAL, NEUTRAL;

        public static HeroType parse(String str) {
            return new EnumParser<>(HeroType.class).parse(str);
        }
    }
}
