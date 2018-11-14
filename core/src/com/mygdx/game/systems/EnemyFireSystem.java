package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.components.BodyComponent;
import com.mygdx.game.components.EnemyStatsComponent;
import com.mygdx.game.components.IsPlayerComponent;
import com.mygdx.game.components.NeedToRemoveComponent;
import com.mygdx.game.entities.Factory;
import com.mygdx.game.utilities.Utilities;

/**
 * A system responsible for spawning bullet for enemies.
 */
public class EnemyFireSystem extends IntervalSystem {

   ImmutableArray<Entity> entities;

   private ComponentMapper<EnemyStatsComponent> escm;
   private ComponentMapper<BodyComponent> bcm;
   public EnemyFireSystem() {
      super(Utilities.MAX_STEP_TIME);
      escm=ComponentMapper.getFor(EnemyStatsComponent.class);
      bcm=ComponentMapper.getFor(BodyComponent.class);
   }

   @Override
   public void addedToEngine(Engine engine) {
     entities= engine.getEntitiesFor(Family.all(EnemyStatsComponent.class, BodyComponent.class).get());
   }
   /**
    * The processing logic of the system should be placed here.
    */
   @Override
   protected void updateInterval() {
      for(Entity entity: entities){
         EnemyStatsComponent es=escm.get(entity);
         BodyComponent bc=bcm.get(entity);
         es.timer+=Utilities.MAX_STEP_TIME;
         if(es.shoot==true && es.timer>=es.rof){
            Entity bullet =Factory.getFactory().createEnemyBullet(bc.body.getPosition().x, bc.body.getPosition().y,es.bulletType);
            ComponentMapper<IsPlayerComponent> isPlayerComponentComponentMapper=ComponentMapper.getFor(IsPlayerComponent.class);
            if(es.aimedAtTarget=true && es.target!=null){
               if(isPlayerComponentComponentMapper.get(es.target)==null){
                  es.aimedAtTarget=false;
                  es.target=null;
                  bullet.getComponent(BodyComponent.class).body.setLinearVelocity(0f,-es.speed);
               }else{
                  Body body=bullet.getComponent(BodyComponent.class).body;
                  Body tBody= es.target.getComponent(BodyComponent.class).body;
                  double x1 = tBody.getPosition().x-body.getPosition().x;
                  double y1 = tBody.getPosition().y-body.getPosition().y;
                  double length= Math.sqrt(x1*x1+ y1*y1);
                  x1=x1/length;
                  y1=y1/length;
                  body.setLinearVelocity((float)x1*Math.abs(es.speed),(float)y1*Math.abs(es.speed));
                  body.setTransform(body.getPosition(),Utilities.vectorToAngle(new Vector2((float)-x1,(float)-y1)));
                  es.target=Factory.getFactory().players.get((MathUtils.random(0,Factory.getFactory().players.size()-1)));
               }
            }else{
               bullet.getComponent(BodyComponent.class).body.setLinearVelocity(0f,-es.speed);
            }
            es.timer=0;
         }
      }
   }
}
