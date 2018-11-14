package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * This component stores data related to input of a player.
 */
public class MovementComponent implements Component, Pool.Poolable {
   public boolean moveLeft, moveRight, moveUp, moveDown, shot, secondary;
   public float moveX, moveY;
//   public boolean shot, secondary;

   /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
        moveLeft=false;
        moveRight=false;
        moveUp=false;
        moveDown=false;
        moveX = 0;
        moveY = 0;
        shot=false;
        secondary=false;
    }
}
