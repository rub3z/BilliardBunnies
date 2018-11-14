package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * This component stores variables related to the intermediate data between rendering and physic system.
 */
public class TransformComponent implements Component, Pool.Poolable {
   public final Vector3 position = new Vector3();
   public final Vector3 previousPosition=new Vector3();
   public final Vector2 scale = new Vector2(1.0f, 1.0f);
   public float rotation = 0.0f;
   public boolean isHidden = false;

    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
        position.set(0,0,0);
        previousPosition.set(0,0,0);
        scale.set(1.0f,1.0f);
        rotation=0.0f;
        isHidden=false;
    }
}

