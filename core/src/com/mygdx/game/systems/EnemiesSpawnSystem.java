package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Factory;
import com.mygdx.game.utilities.Utilities;

/**
 * A system responsible for spawning enemies.
 */
public class EnemiesSpawnSystem extends IteratingSystem {

   Array<EnemySpawnData> enemySpawnData;
   public EnemiesSpawnSystem(){
      super(Family.all().get());
      enemySpawnData=new Array<EnemySpawnData>();
      enemySpawnData.add(new EnemySpawnData(1f/1f) { // 1 enemy per 1second
         @Override
         public void spawn() {
            Factory.getFactory().spawnEnemy1(Utilities.FRUSTUM_WIDTH/2,Utilities.FRUSTUM_HEIGHT+10, "GameScreen/Behaviors/Behavior1.txt");
            Factory.getFactory().spawnEnemy1(Utilities.FRUSTUM_WIDTH/2,Utilities.FRUSTUM_HEIGHT+10, "GameScreen/Behaviors/Behavior2.txt");
         }

         @Override
         public boolean isDone() {
            return true;
         }
      });
      enemySpawnData.add(new EnemySpawnData(3/10f) { // 3 enemies per 10 second
         @Override
         public void spawn() {
            Factory.getFactory().spawnEnemy2(0-10,MathUtils.random(Utilities.FRUSTUM_HEIGHT-10,MathUtils.random(Utilities.FRUSTUM_HEIGHT+10)), "GameScreen/Behaviors/Behavior3.txt");
            Factory.getFactory().spawnEnemy2(Utilities.FRUSTUM_WIDTH+10,MathUtils.random(Utilities.FRUSTUM_HEIGHT-10,MathUtils.random(Utilities.FRUSTUM_HEIGHT+10)), "GameScreen/Behaviors/Behavior4.txt");
         }
         @Override
         public boolean isDone() {
            return true;
         }
      });
      enemySpawnData.add(new EnemySpawnData(1f/60f) { //1 enemy every 60 second
         @Override
         public void spawn() {
            Factory.getFactory().spawnBoss(Utilities.FRUSTUM_WIDTH / 2, Utilities.FRUSTUM_HEIGHT +25, "GameScreen/Behaviors/Behavior5.txt");
            this.accumulator=0;

         }
         @Override
         public boolean isDone() {
            return Factory.getFactory().boss.size() == 0;
         }
      });
   }

   @Override
   public void update(float deltaTime){
      int numPlayer=Factory.getFactory().players.size();
      float scale;
      switch (numPlayer){
         case 2:
            scale=0.2f;
            break;
         case 3:
            scale=0.3f;
            break;
         case 4:
            scale=0.4f;
            break;
            default:
               scale=0;
               break;
      }
      for(EnemySpawnData data: enemySpawnData){
         if(data.isDone()){
            data.accumulator+=deltaTime;
            if(data.accumulator>=data.spawnRate*(1-scale)){
               data.accumulator-=data.spawnRate*(1-scale);
               data.spawn();
            }
         }

      }



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

   }

   /**
    * Abstract enemy spawn data.
    */
   abstract class EnemySpawnData{
      public float spawnRate; //How many enemies per second.
      public float accumulator;
      public EnemySpawnData(float spawnRate){
         this.spawnRate=1/spawnRate;
         accumulator=0;
      }
      public abstract void spawn();
      public abstract boolean isDone();
   }
}
