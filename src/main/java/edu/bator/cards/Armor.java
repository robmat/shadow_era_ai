package edu.bator.cards;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Armor extends Card {

    public Armor() {
    }

    public Armor(Card cloneFrom) {
        super(cloneFrom);
    }

}