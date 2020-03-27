package edu.bator.cards;

import edu.bator.game.GamePhase;

public interface Expirable {

  int getTurnExpires();

  GamePhase getPhaseExpires();
}
