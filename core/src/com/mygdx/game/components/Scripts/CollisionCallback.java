package com.mygdx.game.components.Scripts;


import com.badlogic.ashley.core.Entity;

/**
 * An interface for a collision callback.
 */
public interface CollisionCallback {
   void run(Entity thisObject, Entity otherObject);
}
