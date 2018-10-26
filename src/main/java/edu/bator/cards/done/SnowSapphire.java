package edu.bator.cards.done;

import edu.bator.cards.Armor;
import edu.bator.cards.Card;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SnowSapphire extends Armor {

    public SnowSapphire() {
    }

    public SnowSapphire(Card cloneFrom) {
        super(cloneFrom);
    }
}
