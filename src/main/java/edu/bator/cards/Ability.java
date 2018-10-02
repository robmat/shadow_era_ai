package edu.bator.cards;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ability extends Card {

    public Ability(Card cloneFrom) {
        super(cloneFrom);
    }

}
