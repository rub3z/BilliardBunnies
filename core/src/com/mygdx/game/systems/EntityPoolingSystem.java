package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.components.*;

public class EntityPoolingSystem extends IteratingSystem {

   private World world;
   private Engine engine;
   private ComponentMapper<BodyComponent> bm;
   public EntityPoolingSystem(World world, Engine engine){

      super(Family.all(NeedToPoolComponent.class).get());
      this.world=world;
      this.engine=engine;
      bm=ComponentMapper.getFor(BodyComponent.class);
   }

   /**
    * This method is called on every entity on every update call of the EntitySystem. Override this to implement your system's
    * specific processing.
    *
    * @param entity    The current Entity being processed
    * @param deltaTime The delta time between the last and current frame
    */
   @Override
   protected void processEntity(Entity entity, float deltaTime) {
      if(!world.isLocked()){
         BodyComponent bodyComponent=bm.get(entity);
         if(bodyComponent!=null) {
            if (entity.getComponent(IsBulletComponent.class) != null) {
               entity.getComponent(BodyComponent.class).body.setTransform(200, 200, 0);
               entity.getComponent(BodyComponent.class).body.setLinearVelocity(0, 0);
            }
            if (entity.getComponent(IsEnemyComponent.class) != null) {
               entity.getComponent(BodyComponent.class).body.setTransform(300, 300, 0);
               entity.getComponent(BodyComponent.class).body.setLinearVelocity(0, 0);
            }
         }
         entity.remove(NeedToPoolComponent.class);
      }
   }



}
