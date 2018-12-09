package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.BodyComponent;
import com.mygdx.game.components.EnemyStatsComponent;
import com.mygdx.game.components.IsEnemyComponent;
import com.mygdx.game.utilities.LevelManager;
import com.mygdx.game.utilities.Utilities;

public class AISystem extends IntervalSystem{
   ImmutableArray<Entity> entities;
   private ComponentMapper<EnemyStatsComponent> escm;
   private ComponentMapper<BodyComponent> bcm;
   private ComponentMapper<IsEnemyComponent> em;

   public AISystem() {
      super(Utilities.MAX_STEP_TIME);
      escm=ComponentMapper.getFor(EnemyStatsComponent.class);
      bcm=ComponentMapper.getFor(BodyComponent.class);

   }

   public void addedToEngine(Engine engine) {
      entities= engine.getEntitiesFor(Family.all(EnemyStatsComponent.class, BodyComponent.class).get());
   }

   @Override
   protected void updateInterval() {
      for(Entity entity: entities) {
         EnemyStatsComponent es = escm.get(entity);
         BodyComponent bc = bcm.get(entity);
         float xDist;
         float yDist;
         LevelManager.getManager().getTile(bc.body.getPosition().x, bc.body.getPosition().y).neighbors.get(1);
         xDist = playerPosition(es).x - bc.body.getPosition().x;
         yDist = playerPosition(es).y - bc.body.getPosition().y;
         if (Math.abs(xDist) > Math.abs(yDist)) {

         }
         else {

         }
//         if (es.timer >= 3f) {
//            xDist = playerPosition(es).x - bc.body.getPosition().x;
//            yDist = playerPosition(es).y - bc.body.getPosition().y;
//            if (playerPosition(es).x < bc.body.getPosition().x) {
//               if (playerPosition(es).y > bc.body.getPosition().y) {
//                  if (xDist > yDist)
//                     bc.body.setLinearVelocity(-es.speed, 0);
//                  else
//                     bc.body.setLinearVelocity(0, -es.speed);
//               }
//               else {
//                  if (xDist > yDist)
//                     bc.body.setLinearVelocity(-es.speed,0);
//                  else
//                     bc.body.setLinearVelocity(0,-es.speed);
//               }
//            }
//            else {
//               if (playerPosition(es).y > bc.body.getPosition().y) {
//                  if (xDist > yDist)
//                     bc.body.setLinearVelocity(es.speed, 0);
//                  else
//                     bc.body.setLinearVelocity(0, es.speed);
//               }
//               else {
//                  if (xDist > yDist)
//                     bc.body.setLinearVelocity(es.speed,0);
//                  else
//                     bc.body.setLinearVelocity(0,es.speed);
//               }
//            }
//            es.timer = 0;
//         }
      }
   }

   protected Vector2 playerPosition(EnemyStatsComponent es) {
      return es.target.getComponent(BodyComponent.class).body.getPosition();
   }
}
