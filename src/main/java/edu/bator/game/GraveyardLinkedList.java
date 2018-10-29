package edu.bator.game;

import edu.bator.cards.Card;

import java.util.LinkedList;

public class GraveyardLinkedList extends LinkedList<Card> {

    @Override
    public boolean add(Card card) {
        card.resetFlags();
        card.getEffects().clear();
        return super.add(card);
    }

    @Override
    public Card removeLast() {
        Card card = super.removeLast();
        card.resetAttackAnHp();
        return card;
    }
}
