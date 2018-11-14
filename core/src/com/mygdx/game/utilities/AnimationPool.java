package com.mygdx.game.utilities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * An implementation of Pool for an animation
 */
public class AnimationPool extends Pool<Animation<TextureRegion>> {
   /**
    * Keep track of which animation it is supposed to create.
    */
   private Array<TextureAtlas.AtlasRegion> animation;
   public AnimationPool(Array<TextureAtlas.AtlasRegion> animation){
      this.animation=animation;
   }

   /**
    * Create new animation when need to.
    * @return
    */
   @Override
   protected Animation<TextureRegion> newObject() {
      return new Animation<TextureRegion>(1/60f,animation,Animation.PlayMode.LOOP);
   }
}
