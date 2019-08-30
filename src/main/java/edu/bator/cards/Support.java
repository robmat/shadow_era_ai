package edu.bator.cards;

import edu.bator.cards.util.SupportExpireUtil;
import edu.bator.game.GameState;
import lombok.EqualsAndHashCode;

import static edu.bator.cards.Artifact.artifactOrSupportCast;

@EqualsAndHashCode(callSuper = true)
public class Support extends Card {

    public Support() {
    }

    public Support(Card cloneFrom) {
        super(cloneFrom);
    }

    @Override
    public void supportIsCast(GameState gameState) {
        artifactOrSupportCast(gameState, this);
    }

    @Override
    public void gamePhaseChangeEvent(GameState gameState) {
        if (this instanceof Expirable) {
            SupportExpireUtil.expireCard(gameState, (Expirable) this);
        }
    }
}
