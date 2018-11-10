package edu.bator.cards.util;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.game.GameState;

public class ArmorUtil {
    public static int attachmentModifiesAttack(Card target, Card attackSource, GameState gameState) {
        int initialAttack = attackSource.getAttack(gameState);
        int modifier = target.getAttachments().stream()
                .map(Attachment::armorModifier)
                .reduce(0, (a, b) -> a + b);

        return modifier <= initialAttack ? initialAttack - modifier : 0;
    }
}
