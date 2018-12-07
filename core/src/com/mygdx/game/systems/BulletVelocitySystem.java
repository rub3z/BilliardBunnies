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
 * A system that responsible for spawning bullet.
 */
public class BulletVelocitySystem extends IntervalSystem {
   private ImmutableArray<Entity> entities;
   private ComponentMapper<MovementComponent> mm;
   private ComponentMapper<BodyComponent> bm;
   private ComponentMapper<BulletVelocityStatComponent> bvm;

   public BulletVelocitySystem() {
      super(Utilities.MAX_STEP_TIME);
      mm = ComponentMapper.getFor(MovementComponent.class);
      bm = ComponentMapper.getFor(BodyComponent.class);
      bvm = ComponentMapper.getFor(BulletVelocityStatComponent.class);
   }

   @Override
   public void addedToEngine(Engine engine){
      entities=engine.getEntitiesFor(Family.all(MovementComponent.class).get());
   }


   /**
    * The processing logic of the system should be placed here.
    */
   @Override
   protected void updateInterval() {

      for(Entity entity: entities){
         BulletVelocityStatComponent bvc = bvm.get(entity);
         MovementComponent mC = mm.get(entity);
         BodyComponent bC = bm.get(entity);
         bvc.timer += Utilities.MAX_STEP_TIME;

         if (mC.shot && bvc.timer > bvc.rof) {
            Entity bullet1 = Factory.getFactory().shoot(bC.body.getPosition().x, bC.body.getPosition().y, entity.getComponent(IsPlayerComponent.class).playerNum);
            bullet1.getComponent(BodyComponent.class).body.setLinearVelocity(
             (mC.shootX / (float) Math.sqrt(mC.shootX * mC.shootX + mC.shootY * mC.shootY)) * bvc.movingSpeed,
             (mC.shootY / (float) Math.sqrt(mC.shootX * mC.shootX + mC.shootY * mC.shootY)) * bvc.movingSpeed);
            bvc.timer = 0;
         }
         if(bC.body.getPosition().x > Utilities.FRUSTUM_WIDTH)
            bC.body.setTransform(bC.body.getPosition().x % Utilities.FRUSTUM_WIDTH,
             bC.body.getPosition().y, 0);
         if(bC.body.getPosition().y > Utilities.FRUSTUM_HEIGHT)
            bC.body.setTransform(bC.body.getPosition().x,
             bC.body.getPosition().y % Utilities.FRUSTUM_HEIGHT, 0);
         if(bC.body.getPosition().x < 0)
            bC.body.setTransform(bC.body.getPosition().x + Utilities.FRUSTUM_WIDTH,
             bC.body.getPosition().y, 0);
         if(bC.body.getPosition().y < 0)
            bC.body.setTransform(bC.body.getPosition().x,
             bC.body.getPosition().y + Utilities.FRUSTUM_HEIGHT, 0);
      }
   }
}
