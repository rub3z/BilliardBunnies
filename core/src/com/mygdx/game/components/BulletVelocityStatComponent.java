package com.mygdx.game.components;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * A component that store variables related to a bullet
 */
public class BulletVelocityStatComponent implements Component, Pool.Poolable {
   public float movingSpeed = 50f;
   public float rof = 0.05f;
   public float timer = 0;

    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
        movingSpeed = 50f;
        rof = 0.05f;
        timer = 0;
    }
}
