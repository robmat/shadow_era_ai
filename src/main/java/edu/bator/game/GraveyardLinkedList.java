package edu.bator.game;

import java.util.LinkedList;

import edu.bator.cards.Card;

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
