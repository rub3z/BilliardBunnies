package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.entities.Factory;

/**
 * This component indicates a laser.
 */
public class IsLaserComponent implements Component, Pool.Poolable {
   public int playerNum;
   public Entity laser = null;

   @Override
   public void reset() {
      if(laser != null) {
         laser.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
      }
      laser = null;
   }
}
