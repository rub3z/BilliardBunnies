package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.entities.Factory;

/**
 * This is a representation of sprite in our engine system.
 */
public class TextureComponent implements Component, Pool.Poolable{
   public Animation<TextureRegion> textureRegionAnimation = null;
   public String name=null;

    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
       if(textureRegionAnimation!=null){
          Factory.getFactory().getAnimationManager().getAnimationPool(name).free(textureRegionAnimation);
       }
       textureRegionAnimation=null;
    }
}
