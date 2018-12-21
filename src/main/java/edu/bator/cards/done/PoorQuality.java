package edu.bator.cards.done;

import java.util.Objects;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class PoorQuality extends Attachment {

    public PoorQuality() {
    }

    public PoorQuality(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState state) {
        boolean you = state.yourAction() && (card.equals(state.getEnemyHero().getWeapon()) || card.equals(state.getEnemyHero().getArmor()));
        boolean enemy = state.enemyAction() && (card.equals(state.getYourHero().getWeapon()) || card.equals(state.getYourHero().getArmor()));
        return super.ableToApplyAbilityTo(card, state) && (you || enemy);
    }

    @Override
    public int durabilityInCombatLost() {
        return -1;
    }

    @Override
    public Integer modifiesAttack(Card card, GameState gameState) {
        return Objects.nonNull(card.getArmor()) && card.getArmor().getAttachments().contains(this) || card.cardIsAWeapon() && card.getAttachments().contains(this) ? -1 : 0;
    }
}
