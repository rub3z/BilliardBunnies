package com.mygdx.game.components.Scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.components.IsEnemyBulletComponent;
import com.mygdx.game.components.IsEnemyComponent;
import com.mygdx.game.components.NeedToRemoveComponent;
import com.mygdx.game.entities.Factory;

/**
 * A collision callback for a player.
 */
public class PlayerCollisionCallback implements CollisionCallback, Pool.Poolable {

   @Override
   public void run(Entity thisObject, Entity otherObject) {
      if(otherObject.getComponent(IsEnemyComponent.class)!=null){
         thisObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
      }
      if(otherObject.getComponent(IsEnemyBulletComponent.class)!=null){
         thisObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
      }
   }

   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {

   }
}
