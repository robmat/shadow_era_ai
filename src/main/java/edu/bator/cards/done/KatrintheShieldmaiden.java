package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.util.Objects;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class KatrintheShieldmaiden extends Ally {

    private static final Logger log = Logger.getLogger(KatrintheShieldmaiden.class);
    private Card target;

    public KatrintheShieldmaiden(Card cloneFrom) {
        super(cloneFrom);
        Optional.ofNullable(cloneFrom)
                .filter(KatrintheShieldmaiden.class::isInstance)
                .map(KatrintheShieldmaiden.class::cast)
                .filter(kat -> Objects.nonNull(kat.getTarget()))
                .map(KatrintheShieldmaiden::getTarget)
                .ifPresent(target -> {
                    try {
                        this.target = (Card) target.clone();
                    } catch (CloneNotSupportedException e) {
                        log.error(e.toString(), e);
                    }
                });
    }

    @Override
    public boolean hasAbilityToUse(GameState gameState) {
        boolean you = GamePhase.YOU_ACTION.equals(gameState.getGamePhase()) &&
                gameState.getYourAllies().contains(this) &&
                gameState.getYourAllies().stream().filter(card -> !card.equals(this)).anyMatch(Card::cardIsAnAlly) &&
                !gameState.getYourAllies().contains(target) &&
                gameState.getYourCurrentResources() >= 1;
        boolean enemy = GamePhase.ENEMY_ACTION.equals(gameState.getGamePhase()) &&
                gameState.getEnemyAllies().contains(this) &&
                gameState.getEnemyAllies().stream().filter(card -> !card.equals(this)).anyMatch(Card::cardIsAnAlly) &&
                !gameState.getEnemyAllies().contains(target) &&
                gameState.getEnemyCurrentResources() >= 1;
        boolean abilityReadied = isAbilityReadied();
        return (you || enemy) && abilityReadied;
    }

    @Override
    public boolean ableToApplyAbilityTo(Card card, GameState gameState) {
        boolean inYourHand = gameState.currentYourAlliesBasedOnPhase().contains(card);
        return card.cardIsAnAlly() && inYourHand && !this.equals(card);
    }

    @Override
    public void applyAbility(Card card, GameState gameState) {
        target = card;
        if (Objects.nonNull(target.getCurrentHp(gameState))) {
            target.setCurrentHp(target.currentHpWithoutBonus() + 2);
        }
    }
}