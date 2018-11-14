package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.game.components.Scripts.CollisionCallback;
import com.mygdx.game.components.Scripts.EnemyCollisionCallback;
import com.mygdx.game.components.Scripts.InvisibleWallCollisionCallback;
import com.mygdx.game.components.Scripts.PlayerCollisionCallback;

/**
 * This component stores the callback that show occur when collision happen
 */
public class CollisionCallbackComponent implements Component , Pool.Poolable {
   public CollisionCallback beginContactCallback=null;
   public CollisionCallback endContactCallback=null;

    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
       if(beginContactCallback!=null &&beginContactCallback instanceof EnemyCollisionCallback){
          Pools.get(EnemyCollisionCallback.class).free((EnemyCollisionCallback)beginContactCallback);
       }
       if(beginContactCallback!=null &&beginContactCallback instanceof PlayerCollisionCallback){
          Pools.get(PlayerCollisionCallback.class).free((PlayerCollisionCallback)beginContactCallback);
       }
       if(beginContactCallback!=null &&beginContactCallback instanceof InvisibleWallCollisionCallback){
          Pools.get(InvisibleWallCollisionCallback.class).free((InvisibleWallCollisionCallback)beginContactCallback);
       }

       if(endContactCallback!=null &&endContactCallback instanceof EnemyCollisionCallback){
          Pools.get(EnemyCollisionCallback.class).free((EnemyCollisionCallback)beginContactCallback);
       }
       if(endContactCallback!=null &&endContactCallback instanceof PlayerCollisionCallback){
          Pools.get(PlayerCollisionCallback.class).free((PlayerCollisionCallback)beginContactCallback);
       }
       if(endContactCallback!=null &&endContactCallback instanceof InvisibleWallCollisionCallback){
          Pools.get(InvisibleWallCollisionCallback.class).free((InvisibleWallCollisionCallback)beginContactCallback);
       }
        beginContactCallback=null;
        endContactCallback=null;
    }
}
