package edu.bator.cards.util;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.game.GameState;

public class ArmorUtil {
    public static int attachmentModifiesAttack(Card target, Card attackSource, GameState gameState) {
        int initialAttack = attackSource.getAttack(gameState);
        return calculateDamage(target, initialAttack);
    }

    public static Integer attachmentModifiesAbilityDamage(Card target, int damage, GameState gameState) {
        return calculateDamage(target, damage);
    }

    private static Integer calculateDamage(Card target, int damage) {
        int modifier = target.getAttachments().stream()
                .map(Attachment::armorModifier)
                .reduce(0, (a, b) -> a + b);

        return modifier <= damage ? damage - modifier : 0;
    }
}
