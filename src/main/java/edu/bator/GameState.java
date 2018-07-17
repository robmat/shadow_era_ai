package edu.bator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.bator.cards.AllCardsSet;
import edu.bator.cards.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameState {
    int currentTurn;

    Card enemyHero;
    Card yourHero;

    Queue<Card> enemyCards = new LinkedList<>();
    Queue<Card> yourCards = new LinkedList<>();

    List<Card> enemyHands = new LinkedList<>();
    List<Card> yourHands = new LinkedList<>();

    AllCardsSet allCardsSet = new AllCardsSet();

    public void init() {
        enemyHero = allCardsSet.findByName("Boris Skullcrusher");
        yourHero = allCardsSet.findByName("Boris Skullcrusher");
    }
}
