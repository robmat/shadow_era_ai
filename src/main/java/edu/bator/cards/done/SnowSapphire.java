package edu.bator.cards.done;

import edu.bator.cards.Armor;
import edu.bator.cards.Card;
import edu.bator.cards.effects.FrozenEffect;
import edu.bator.game.GamePhase;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;
import org.apache.log4j.Logger;

import static java.lang.String.format;

@EqualsAndHashCode(callSuper = true)
public class SnowSapphire extends Armor {

    private static final Logger log = Logger.getLogger(SnowSapphire.class);

    public SnowSapphire() {
    }

    public SnowSapphire(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void armorEffectDuringAttack(Card target, Card source, GameState gameState) {
        GamePhase gamePhase = gameState.getGamePhase();
        int turnWhenExpires = gameState.getCurrentTurn() + 1;
        if (gamePhase.equals(GamePhase.YOU_ACTION)) {
            gamePhase = GamePhase.ENEMY_PREPARE;
        }
        if (gamePhase.equals(GamePhase.ENEMY_ACTION)) {
            gamePhase = GamePhase.YOU_PREPARE;
            turnWhenExpires++;
        }
        source.getEffects().add(new FrozenEffect(turnWhenExpires, gamePhase));
        log.debug(format("%s has effects %s", source, source.getEffects()));
    }
}
