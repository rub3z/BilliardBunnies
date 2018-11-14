package com.mygdx.game.utilities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

/**
 * An manager that manages multiple set of animation
 */
public class AnimationManager {

   /**
    * Maps to all animation pools
    */
   private IntMap<AnimationPool> animationPools;

   public AnimationManager(){
      animationPools=new IntMap<AnimationPool>();
   }

   /**
    * Add new animation pools
    * @param type
    * @param animation
    */
   public void addNewAnimation(int type, Array<TextureAtlas.AtlasRegion> animation){
      animationPools.put(type,new AnimationPool(animation));
   }

   /**
    * Get animation from pools
    * @param type of animation
    * @param textures animation that you want to animate
    * @return animation
    */
   public Animation<TextureRegion> getPooledAnimation(String type, Array<TextureAtlas.AtlasRegion> textures){
      if(!animationPools.containsKey(type.hashCode())){
         addNewAnimation(type.hashCode(),textures);
      }
      Animation<TextureRegion> animation=animationPools.get(type.hashCode()).obtain();
      animation.getKeyFrame(0); //Reset animation to default
      return animation;
   }

   /**
    * Get pool that contains that a specific type of animation
    * @param type type of animation
    * @return an animation pool
    */
   public AnimationPool getAnimationPool(String type){
      if(animationPools.containsKey(type.hashCode())){
         return animationPools.get(type.hashCode());
      }
      return null;
   }
}
