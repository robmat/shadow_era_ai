package edu.bator.game;

import java.util.LinkedList;

import edu.bator.cards.Card;

public class GraveyardLinkedList extends LinkedList<Card> {

    @Override
    public boolean add(Card card) {
        card.resetFlags();
        return super.add(card);
    }
}
