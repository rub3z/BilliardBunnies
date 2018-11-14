package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.ParticleEffectDataComponent;

/**
 * A system responsible for rendering particle effects
 */
public class ParticleEffectSystem extends IteratingSystem {
   private static final boolean shouldRender = true;

   private Array<Entity> renderQueue;
   private SpriteBatch batch;
   private OrthographicCamera camera;
   private ComponentMapper<ParticleEffectDataComponent> peM;

   /**
    * Instantiates a system that will iterate over the entities described by the Family.
    *
    */
   public ParticleEffectSystem(SpriteBatch batch, OrthographicCamera camera) {
      super(Family.all(ParticleEffectDataComponent.class).get());
      this.batch=batch;
      this.camera=camera;
      renderQueue=new Array<Entity>();
   }

   @Override
   public void addedToEngine(Engine engine) {
      super.addedToEngine(engine);
      peM=ComponentMapper.getFor(ParticleEffectDataComponent.class);
   }

   @Override
   public void update(float deltaTime) {
      super.update(deltaTime);
      batch.setProjectionMatrix(camera.combined);
      batch.enableBlending();
      if(shouldRender){
         batch.begin();
         for (Entity entity : renderQueue) {
            ParticleEffectDataComponent particleEffectComponent = peM.get(entity);
            particleEffectComponent.particleEffect.draw(batch, deltaTime);
         }
         batch.end();
      }
      renderQueue.clear();
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
      ParticleEffectDataComponent particleEffectComponent=peM.get(entity);

      //Looped
      if(particleEffectComponent.isLooped && particleEffectComponent.particleEffect.isComplete()){
         particleEffectComponent.particleEffect.reset();
      }

      //Start deadtimer
      if(particleEffectComponent.isDead){
         particleEffectComponent.timeTillDeath -= deltaTime;
      }

      //Update position is attached to body
      if(particleEffectComponent.isAttached){

         particleEffectComponent.particleEffect.setPosition(particleEffectComponent.attachedBody.getPosition().x+ particleEffectComponent.xOffset,
                 particleEffectComponent.attachedBody.getPosition().y+particleEffectComponent.yOffset);
      }

      if(particleEffectComponent.timeTillDeath <= 0||(particleEffectComponent.particleEffect.isComplete() &&!particleEffectComponent.isLooped)){
         getEngine().removeEntity(entity);
      }else{
         renderQueue.add(entity);
      }

   }
}
