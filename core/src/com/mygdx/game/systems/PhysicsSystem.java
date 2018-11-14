package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.components.BodyComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.utilities.Utilities;

/**
 * A system responsible for physic calculation
 */
public class PhysicsSystem extends IntervalSystem {

   ImmutableArray<Entity> entities;
   private World world;
   private ComponentMapper<BodyComponent> bm;
   private ComponentMapper<TransformComponent> tm;

   public PhysicsSystem(World world) {
      super(Utilities.MAX_STEP_TIME);

      //Mappers
      bm = ComponentMapper.getFor(BodyComponent.class);
      tm = ComponentMapper.getFor(TransformComponent.class);

      //World
      this.world = world;
   }

   @Override
   public void addedToEngine(Engine engine) {
      entities = engine.getEntitiesFor(Family.all(BodyComponent.class, TransformComponent.class).get());
   }

   /**
    * The processing logic of the system should be placed here.
    */
   @Override
   protected void updateInterval() {
       for (Entity entity : entities) {
           TransformComponent transformComponent = tm.get(entity);
           transformComponent.previousPosition.x=transformComponent.position.x;
           transformComponent.previousPosition.y=transformComponent.position.y;

       }
      world.step(Utilities.MAX_STEP_TIME, Utilities.VELOCITY_ITERATIONS, Utilities.POSITION_ITERATIONS);
      for (Entity entity : entities) {
         TransformComponent transformComponent = tm.get(entity);
         BodyComponent bodyComponent = bm.get(entity);
         Vector2 position = bodyComponent.body.getPosition();
         transformComponent.position.x = (position.x);
         transformComponent.position.y = (position.y);
         transformComponent.rotation = bodyComponent.body.getAngle() * MathUtils.radiansToDegrees;
      }
   }
}
