package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * This component stores particle effect that attached to this entity.
 */
public class ParticleEffectComponent  implements Component,Pool.Poolable {
   public Entity effect;

   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {
      if(effect!=null){
         if(effect.getComponent(ParticleEffectDataComponent.class)!=null)
            effect.getComponent(ParticleEffectDataComponent.class).isDead=true;
         effect=null;
      }
   }
}
