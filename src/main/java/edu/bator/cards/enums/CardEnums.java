package edu.bator.cards.enums;

import edu.bator.enums.EnumParser;

public class CardEnums {

    public enum Ability {
        STEALTH, AMBUSH, DEFENDER, HASTE, PROTECTOR;

        public static Ability parse(String str) {
            return new EnumParser<>(Ability.class).parse(str);
        }
    }

    public enum AttackType {
        CLAW, SWORD, ICE, ELECTRIC, FIRE, BOW, ARCANE;

        public static AttackType parse(String str) {
            return new EnumParser<>(AttackType.class).parse(str);
        }
    }

    public enum ItemSubType {
        WEAPON, ARMOR, ARTIFACT, TRAP;

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

    public enum HeroClass {
        WARRIOR, PRIEST, ROGUE, MAGE, HUNTER, VULVEN, ELEMENTAL, NEUTRAL;

        public static HeroClass parse(String str) {
            return new EnumParser<>(HeroClass.class).parse(str);
        }
    }
}
