package edu.bator.cards;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

import edu.bator.game.GameState;

import static java.util.Objects.nonNull;

public class Ability extends Card {

    public Ability(Card cloneFrom) {
        super(cloneFrom);
    }

}
