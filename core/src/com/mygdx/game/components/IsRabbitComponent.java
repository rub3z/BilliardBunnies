package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class IsRabbitComponent implements Component,Pool.Poolable {
   public int direction=-10;
   public static int moveLeft=0;
   public static int moveRight=1;
   public static int moveUp=2;
   public static int moveDown=3;


   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {
      direction=-10;
   }
}
