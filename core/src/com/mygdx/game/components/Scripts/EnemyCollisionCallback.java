package com.mygdx.game.components.Scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.components.*;
import com.mygdx.game.entities.Factory;
import com.mygdx.game.screens.GameScreen;
import java.util.Random;
import com.mygdx.game.utilities.ParticleEffectManager;

/**
 * Collision callback for an enemy
 */
public class EnemyCollisionCallback  implements CollisionCallback, Pool.Poolable {
   Random rand = new Random();
   int randomNum = rand.nextInt(100);
   @Override
   public void run(Entity thisObject, Entity otherObject) {
      int numPlayer=Factory.getFactory().players.size();
      float scale=1;
       if(otherObject.getComponent(IsBulletComponent.class)!=null){
         if (randomNum < 10) {
            //thisObject.getComponent(EnemyStatsComponent.class).buffTimer = 10f;
            //thisObject.getComponent(EnemyStatsComponent.class).isBuffed = true;
         }
         randomNum = rand.nextInt(100);
       }
   }

   private void updateScore(int playerNum) {
      switch (playerNum){
         case 0:
            GameScreen.getGameScreen().score0 += 10f;
            GameScreen.getGameScreen().ui
                    .updateScore(0, GameScreen.getGameScreen().score0);
            return;
         case 1:
            GameScreen.getGameScreen().score1 += 10f;
            GameScreen.getGameScreen().ui
                    .updateScore(1, GameScreen.getGameScreen().score1);
            break;
         case 2:
            GameScreen.getGameScreen().score2 += 10f;
            GameScreen.getGameScreen().ui
                    .updateScore(2, GameScreen.getGameScreen().score2);
            break;
         case 3:
            GameScreen.getGameScreen().score3 += 10f;
            GameScreen.getGameScreen().ui
                    .updateScore(3 , GameScreen.getGameScreen().score3);
            break;
      }
   }

   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {

   }
}
