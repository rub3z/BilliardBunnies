package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * A system responsible for rendering hitbox for debugging.
 */
public class PhysicsDebugSystem extends IteratingSystem {

   private Box2DDebugRenderer box2DDebugRenderer;
   private World world;
   private OrthographicCamera camera;

   public PhysicsDebugSystem(World world, OrthographicCamera camera) {
      super(Family.all().get());
      box2DDebugRenderer = new Box2DDebugRenderer();
      this.world = world;
      this.camera = camera;

   }

   @Override
   public void update(float deltaTime) {
      box2DDebugRenderer.render(world, camera.combined);
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

   }

}
