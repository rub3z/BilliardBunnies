package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.components.Scripts.Behavior;

/**
 * This component store a set of player behavior
 */
public class BehaviorComponent implements Component, Pool.Poolable {
   public Array<Behavior> behaviors=null;
   public int count=0;
   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {
      behaviors=null;
      count=0;
   }
}
