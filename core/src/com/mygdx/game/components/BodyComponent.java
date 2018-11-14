package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

/**
 * This is a representation of physical body in our engine.
 */
public class BodyComponent implements Component, Pool.Poolable {

   public Body body = null;

    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
        body=null;
    }
}
