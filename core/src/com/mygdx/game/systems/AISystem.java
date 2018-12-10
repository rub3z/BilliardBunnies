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
import com.mygdx.game.utilities.Tile;
import com.mygdx.game.utilities.Utilities;

public class AISystem extends IntervalSystem{
   ImmutableArray<Entity> entities;
   private ComponentMapper<EnemyStatsComponent> escm;
   private ComponentMapper<BodyComponent> bcm;
   private ComponentMapper<IsEnemyComponent> em;
   private float xDist;
   private float yDist;


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
         es.fireTimer += Utilities.MAX_STEP_TIME;
//         es.currentTile =
//          LevelManager.getManager().getTile(bc.body.getPosition().x, bc.body.getPosition().y);
//         es.timer += 0.1f;
//         if(es.previousTile == null || (es.currentTile != es.previousTile) && es.timer > 1) {
//            //System.out.println("trying to move");
//            xDist = playerPosition(es).x - bc.body.getPosition().x;
//            yDist = playerPosition(es).y - bc.body.getPosition().y;
//            if (Math.abs(xDist) > Math.abs(yDist)) {
//               //prefer left
//               if(xDist < 0) {
//                  setVelocity(es, bc, 1);
//               }
//               //prefer right
//               else {
//                  setVelocity(es, bc, 2);
//               }
//            }
//            else {
//               //prefer up
//               if (yDist < 0) {
//                  setVelocity(es, bc, 3);
//               }
//               //prefer down
//               else {
//                  setVelocity(es, bc, 4);
//               }
//            }
//
//            es.timer = 0;
//         }
         if (es.fireTimer >= 3f) {
            xDist = playerPosition(es).x - bc.body.getPosition().x;
            yDist = playerPosition(es).y - bc.body.getPosition().y;
            if (playerPosition(es).x < bc.body.getPosition().x) {
               if (playerPosition(es).y > bc.body.getPosition().y) {
                  if (xDist > yDist)
                     bc.body.setLinearVelocity(-es.speed, 0);
                  else
                     bc.body.setLinearVelocity(0, -es.speed);
               }
               else {
                  if (xDist > yDist)
                     bc.body.setLinearVelocity(-es.speed,0);
                  else
                     bc.body.setLinearVelocity(0,-es.speed);
               }
            }
            else {
               if (playerPosition(es).y > bc.body.getPosition().y) {
                  if (xDist > yDist)
                     bc.body.setLinearVelocity(es.speed, 0);
                  else
                     bc.body.setLinearVelocity(0, es.speed);
               }
               else {
                  if (xDist > yDist)
                     bc.body.setLinearVelocity(es.speed,0);
                  else
                     bc.body.setLinearVelocity(0,es.speed);
               }
            }
            es.fireTimer = 0;
         }
      }
   }

   protected Vector2 playerPosition(EnemyStatsComponent es) {
      return es.target.getComponent(BodyComponent.class).body.getPosition();
   }

   protected void setVelocity(EnemyStatsComponent es, BodyComponent bc, int prefer) {
      if(es.currentTile.neighbors.get(prefer).getType()
       == LevelManager.Type.EMPTY_SPACE) {
         switch(prefer) {
            case 1:
               System.out.println("attempt left");
               bc.body.setLinearVelocity(-es.speed, 10);
               es.otherDirection = 2;
               break;
            case 2:
               System.out.println("attempt right");
               bc.body.setLinearVelocity(es.speed, -10);
               es.otherDirection = 1;
               break;
            case 3:
               System.out.println("attempt up");
               bc.body.setLinearVelocity(10, es.speed);
               es.otherDirection = 4;
               break;
            case 4:
               System.out.println("attempt down");
               bc.body.setLinearVelocity(-10, -es.speed);
               es.otherDirection = 3;
               break;
            default:
               System.out.println("movement error");
               break;
         }
      }
      else {
         for(int i = 1; i < 5; i++) {
            if(es.currentTile.neighbors.get(i).getType()
             == LevelManager.Type.EMPTY_SPACE) {
               switch(i) {
                  case 1:
                     bc.body.setLinearVelocity(-es.speed, 0);
                     es.otherDirection = 2;
                     break;
                  case 2:
                     bc.body.setLinearVelocity(es.speed, 0);
                     es.otherDirection = 1;
                     break;
                  case 3:
                     bc.body.setLinearVelocity(0, es.speed);
                     es.otherDirection = 4;
                     break;
                  case 4:
                     bc.body.setLinearVelocity(0, -es.speed);
                     es.otherDirection = 3;
                     break;
                  default:
                     System.out.println("movement error");
                     break;
               }
            }
         }
      }
   }
}
