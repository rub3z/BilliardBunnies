package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

/**
 * This component stores variable related to a particle effect.
 */
public class ParticleEffectDataComponent implements Component,Pool.Poolable {
   public ParticleEffectPool.PooledEffect particleEffect;
   public boolean isAttached =false;
   public boolean isLooped=false;
   public float xOffset=0;
   public float yOffset=0;
   public float x=0;
   public float y=0;
   public float timeTillDeath=0.01f;
   public boolean isDead=false;
   public Body attachedBody;

   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {
      particleEffect.free();
      particleEffect=null;
      xOffset=0f;
      yOffset=0f;
      isAttached=false;
      isDead=false;
      attachedBody=null;
      timeTillDeath=0.01f;
      isLooped=false;
   }
}
