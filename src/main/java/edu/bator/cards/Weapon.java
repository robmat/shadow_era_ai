package edu.bator.cards;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Weapon extends Card {

    public Weapon(Card cloneFrom) {
        super(cloneFrom);
    }
}
