package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.components.*;
import com.mygdx.game.entities.Factory;
import com.mygdx.game.utilities.Utilities;

/**
 * A system responsible for spawning laser for a player.
 */
public class LaserSystem extends IntervalSystem {
   private ImmutableArray<Entity> entities;
   private ComponentMapper<MovementComponent> mm;
   private ComponentMapper<BodyComponent> bm;
   private ComponentMapper<IsLaserComponent> lm;

   public LaserSystem() {
      super(Utilities.MAX_STEP_TIME);
      mm = ComponentMapper.getFor(MovementComponent.class);
      bm = ComponentMapper.getFor(BodyComponent.class);
      lm = ComponentMapper.getFor(IsLaserComponent.class);
   }

   @Override
   public void addedToEngine(Engine engine){
      entities=engine.getEntitiesFor(Family.all(MovementComponent.class,IsPlayerComponent.class).get());
   }


   /**
    * The processing logic of the system should be placed here.
    */
   @Override
   protected void updateInterval() {

      for(Entity entity: entities){
         MovementComponent mC = mm.get(entity);
         BodyComponent bC = bm.get(entity);
         IsLaserComponent lc = lm.get(entity);
         if (mC.secondary) {
            if (lc.laser == null) {
               lc.laser = Factory.getFactory().laser(bC.body.getPosition().x, bC.body.getPosition().y + 33,
                entity.getComponent(IsPlayerComponent.class).playerNum);
            }
            else {
               lc.laser.getComponent(BodyComponent.class).body.setTransform(
                bC.body.getPosition().x - 0.5f + ((float)Math.random()), //this adds a neat lazor wiggle
                bC.body.getPosition().y +33,
                0);
            }
         }
         else {
            if (lc.laser != null) {
               lc.laser.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
               lc.laser = null;
            }
         }
      }
   }
}
