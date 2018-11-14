package com.mygdx.game.components.Scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.components.*;
import com.mygdx.game.entities.Factory;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utilities.ParticleEffectManager;

/**
 * Collision callback for an enemy
 */
public class EnemyCollisionCallback  implements CollisionCallback, Pool.Poolable {

   @Override
   public void run(Entity thisObject, Entity otherObject) {
      int numPlayer=Factory.getFactory().players.size();
      float scale=1;
      switch (numPlayer){
         case 1:
            scale=1;
            break;
         case 2:
            scale=0.61f;
            break;
         case 3:
            scale=0.4333f;
            break;
         case 4:
            scale=0.385f;
            break;
      }
       if(otherObject.getComponent(IsBulletComponent.class)!=null){
          if(thisObject.getComponent(EnemyStatsComponent.class).health >= 0) {
             thisObject.getComponent(EnemyStatsComponent.class).health -= 100*scale;
             otherObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
          }
          else {
             Factory.getFactory().createParticleEffect(ParticleEffectManager.CANDYCORNEXPLOSION,
                  thisObject.getComponent(BodyComponent.class).body.getPosition().x,
                  thisObject.getComponent(BodyComponent.class).body.getPosition().y
          );
             updateScore(otherObject.getComponent(IsBulletComponent.class).playerNum);
             thisObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
             otherObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
          }
       }

      if(otherObject.getComponent(IsLaserComponent.class)!=null){
         if(thisObject.getComponent(EnemyStatsComponent.class).health >= 0) {
            thisObject.getComponent(EnemyStatsComponent.class).health -= 200*scale;
         }
         else {
            updateScore(otherObject.getComponent(IsLaserComponent.class).playerNum);
         Factory.getFactory().createParticleEffect(ParticleEffectManager.CANDYCORNEXPLOSION,
                 thisObject.getComponent(BodyComponent.class).body.getPosition().x,
                 thisObject.getComponent(BodyComponent.class).body.getPosition().y
         );

            thisObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
         }
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
