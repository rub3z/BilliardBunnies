package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.components.BulletVelocityStatComponent;
import com.mygdx.game.components.EnemyStatsComponent;
import com.mygdx.game.components.IsPlayerComponent;
import com.mygdx.game.components.PlayerVelocityStatComponent;
import com.mygdx.game.utilities.Utilities;

public class BuffSystem extends IntervalSystem {
   ImmutableArray<Entity> entities;
   private ComponentMapper<EnemyStatsComponent> em;
   private ComponentMapper<BulletVelocityStatComponent> bvsm;
   private ComponentMapper<PlayerVelocityStatComponent> pvsm;

   public BuffSystem() {
      super(Utilities.MAX_STEP_TIME);
      em = ComponentMapper.getFor(EnemyStatsComponent.class);
      bvsm = ComponentMapper.getFor(BulletVelocityStatComponent.class);
      pvsm = ComponentMapper.getFor(PlayerVelocityStatComponent.class);
   }

   @Override
   public void addedToEngine(Engine engine){
      entities=engine.getEntitiesFor(Family.all(EnemyStatsComponent.class, IsPlayerComponent.class).get());
   }

   @Override
   protected void updateInterval() {
      for (Entity entity : entities) {
         EnemyStatsComponent esc = em.get(entity);
         PlayerVelocityStatComponent pvs = pvsm.get(entity);
         BulletVelocityStatComponent bvs = bvsm.get(entity);
         if(esc.buffTimer > 0f) {
            if(esc.buffType == 1) {
               pvs.movingSpeed = 22.5f;
               bvs.rof = 0.5f;
               esc.buffTimer -= Utilities.MAX_STEP_TIME;
            }
            else if(esc.buffType == 2) {
               pvs.movingSpeed = 5f;
               esc.buffTimer -= Utilities.MAX_STEP_TIME;
            }
         }
         else{
            esc.buffType = 0;
            pvs.movingSpeed = 20f;
            bvs.rof = 1f;
         }
      }
   }
}
