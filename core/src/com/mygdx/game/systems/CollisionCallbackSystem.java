package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.components.CollisionCallbackComponent;

/**
 * A system responsible for calling a collision callback
 */
public class CollisionCallbackSystem implements ContactListener {

   private World world;
   private ComponentMapper<CollisionCallbackComponent> ccc;

   public CollisionCallbackSystem(World world) {
      this.world = world;
      world.setContactListener(this);
      ccc = ComponentMapper.getFor(CollisionCallbackComponent.class);
   }

   /**
    * Called when two fixtures begin to touch.
    *
    * @param contact
    */
   @Override
   public void beginContact(Contact contact) {
      Fixture fixtureA = contact.getFixtureA();
      Entity entityA = (Entity) fixtureA.getBody().getUserData();
      Fixture fixtureB = contact.getFixtureB();
      Entity entityB = (Entity) fixtureB.getBody().getUserData();

      if (ccc.get(entityA)!=null &&ccc.get(entityA).beginContactCallback != null) {
         ccc.get(entityA).beginContactCallback.run(entityA, entityB);
      }
      if (ccc.get(entityB)!=null && ccc.get(entityB).beginContactCallback != null) {
         ccc.get(entityB).beginContactCallback.run(entityB, entityA);
      }
   }

   /**
    * Called when two fixtures cease to touch.
    *
    * @param contact
    */
   @Override
   public void endContact(Contact contact) {
      Fixture fixtureA = contact.getFixtureA();
      Entity entityA = (Entity) fixtureA.getBody().getUserData();
      Fixture fixtureB = contact.getFixtureB();
      Entity entityB = (Entity) fixtureB.getBody().getUserData();
      if (ccc.get(entityA)!=null && ccc.get(entityA).endContactCallback != null) {
         ccc.get(entityA).endContactCallback.run(entityA, entityB);
      }
      if (ccc.get(entityB)!=null && ccc.get(entityB).endContactCallback != null) {
         ccc.get(entityB).endContactCallback.run(entityB, entityA);
      }
   }

   @Override
   public void preSolve(Contact contact, Manifold oldManifold) {

   }

   @Override
   public void postSolve(Contact contact, ContactImpulse impulse) {

   }
}
