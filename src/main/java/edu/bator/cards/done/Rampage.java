package edu.bator.cards.done;

import edu.bator.cards.Attachment;
import edu.bator.cards.Card;
import edu.bator.cards.util.BonusUtil;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Rampage extends Attachment {

    public Rampage() {
    }

    public Rampage(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        return super.ableToApplyAbilityTo(card, gameState) && gameState.cardIsCurrentHero(card);
    }

    @Override
    public void cardHasDiedEvent(Card card, GameState gs) {
        if (card.cardIsAnAlly()) {
            Card yourHero = gs.getYourHero();
            boolean you = yourHero.getAttachments().contains(this) &&
                    gs.getEnemyGraveyard().contains(card);
            healTwoHp(gs, you, yourHero);

            Card enemyHero = gs.getEnemyHero();
            boolean enemy = enemyHero.getAttachments().contains(this) &
                    gs.getYourGraveyard().contains(card);
            healTwoHp(gs, enemy, enemyHero);
        }
    }

    private void healTwoHp(GameState gs, boolean you, Card hero) {
        Integer initialHp = hero.getInitialHp();
        if (you && hero.getCurrentHp(gs) < initialHp) {
            hero.setCurrentHp(hero.getCurrentHp(gs) + 2 >= initialHp ? initialHp - BonusUtil.hpBonus(gs, hero) : hero.currentHpWithoutBonus() + 2);
        }
    }
}
