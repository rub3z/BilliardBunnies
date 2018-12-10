package com.mygdx.game.components.Scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.components.*;
import com.mygdx.game.entities.Factory;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utilities.ParticleEffectManager;

import java.util.Random;

/**
 * A collision callback for a player.
 */
public class PlayerCollisionCallback implements CollisionCallback, Pool.Poolable {

   Random rand = new Random();
   int randomNum;

   @Override
   public void run(Entity thisObject, Entity otherObject) {
      if(otherObject.getComponent(EnemyStatsComponent.class)!=null){
         if(thisObject.getComponent(IsHeroComponent.class)!=null){
             thisObject.getComponent(IsPlayerComponent.class).health=0;
         }
       
      }
      if(otherObject.getComponent(IsBulletComponent.class)!=null){
         if(thisObject.getComponent(EnemyStatsComponent.class) != null) {
            if(thisObject.getComponent(EnemyStatsComponent.class).buffType == 0) {
               randomNum = rand.nextInt(100);
               if (randomNum < 10) {
                  thisObject.getComponent(EnemyStatsComponent.class).buffType = 1;
                  thisObject.getComponent(EnemyStatsComponent.class).buffTimer = 5f;
               }
               else if(randomNum < 60) {
                  thisObject.getComponent(EnemyStatsComponent.class).buffType = 2;
                  thisObject.getComponent(EnemyStatsComponent.class).buffTimer = 1f;
               }
            }
         }
         else if(!thisObject.getComponent(IsPlayerComponent.class).isEnemy ){
            thisObject.getComponent(IsPlayerComponent.class).health -= 1;
            updateHealthCounter();
            System.out.println("health:" + thisObject.getComponent(IsPlayerComponent.class).health);
         }else{
            thisObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
         }
         otherObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
      }

      if(otherObject.getComponent(IsSeedComponent.class)!=null){
          System.out.println("Hitting seed");
          otherObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
      }

   }

   private void updateHealthCounter() {
      GameScreen.getGameScreen().score0--;
      GameScreen.getGameScreen().ui.updateScore(0, GameScreen.getGameScreen().score0);
      return;
   }


   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {

   }
}
