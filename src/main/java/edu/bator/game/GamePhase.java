package edu.bator.game;

import lombok.Getter;

public enum GamePhase {
    YOU_PREPARE,
    YOU_SACRIFICE,
    YOU_ACTION,
    ENEMY_PREPARE,
    ENEMY_SACRIFICE,
    ENEMY_ACTION;

    @Getter
    private GamePhase oppositePhase;

    static {
        YOU_PREPARE.oppositePhase = ENEMY_PREPARE;
        YOU_SACRIFICE.oppositePhase = ENEMY_SACRIFICE;
        YOU_ACTION.oppositePhase = ENEMY_ACTION;
        ENEMY_PREPARE.oppositePhase = YOU_PREPARE;
        ENEMY_SACRIFICE.oppositePhase = YOU_SACRIFICE;
        ENEMY_ACTION.oppositePhase = YOU_ACTION;
    }
}
