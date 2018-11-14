package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * This component stores variables related to player's velocity.
 */
public class PlayerVelocityStatComponent implements Component, Pool.Poolable {
    public float movingSpeed = 30f;

    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
        movingSpeed = 30f;
    }
}

