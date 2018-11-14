package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * This component indicates a player.
 */
public class IsPlayerComponent implements Component , Pool.Poolable {
    public boolean isPlayer0, isPlayer1, isPlayer2, isPlayer3 = false;
    public int playerNum=-1;

    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
        isPlayer0 = false;
        isPlayer1 = false;
        isPlayer2 = false;
        isPlayer3 = false;
        playerNum=-1;
    }
}

