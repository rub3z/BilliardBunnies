package com.mygdx.game.components.Scripts;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.components.*;
import com.mygdx.game.entities.Factory;

/**
 * Collision callback for an invisible wall.
 */
public class InvisibleWallCollisionCallback implements CollisionCallback, Pool.Poolable {
    private ComponentMapper<IsBulletComponent> isBulletComponentComponentMapper= ComponentMapper.getFor(IsBulletComponent.class);
    private ComponentMapper<IsEnemyBulletComponent>isEnemyBulletComponentComponentMapper=ComponentMapper.getFor(IsEnemyBulletComponent.class);
   private ComponentMapper<IsEnemyComponent>isEnemyComponentComponentMapper=ComponentMapper.getFor(IsEnemyComponent.class);
   private ComponentMapper<IsBossComponent>isBossComponentComponentMapper=ComponentMapper.getFor(IsBossComponent.class);
    @Override
    public void run(Entity thisObject, Entity otherObject) {
        if(isBulletComponentComponentMapper.get(otherObject)!=null){
            otherObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));

        }
        if(isEnemyBulletComponentComponentMapper.get(otherObject)!=null){
           otherObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
        }
       if(isEnemyComponentComponentMapper.get(otherObject)!=null &&isBossComponentComponentMapper.get(otherObject)==null){
          otherObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
       }
    }

   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {

   }
}
