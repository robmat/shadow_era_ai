package edu.bator.cards.done;

import java.util.Set;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.cards.enums.CardEnums;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class WarriorTraining extends Attachment {

    public WarriorTraining() {
    }

    public WarriorTraining(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return card.cardIsAnAlly() &&
                super.ableToApplyAbilityTo(card, gameState) &&
                (gameState.getYourHand().contains(this) && gameState.getYourAllies().contains(card) ||
                        gameState.getEnemyHand().contains(this) && gameState.getEnemyAllies().contains(card));
    }

    @Override
    public void modifiesAbilities(Set<CardEnums.Ability> abilities, Card card, GameState gameState) {
        if (card.getAttachments().contains(this)) {
            abilities.add(CardEnums.Ability.PROTECTOR);
        }
    }
}
