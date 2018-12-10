package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.mygdx.game.components.IsPlayerComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.utilities.Utilities;

/**
 * A system responsible for translate raw input into in-game input
 */
public class PlayerControlSystem extends IntervalSystem {
   ImmutableArray<Entity> entities;
   private ComponentMapper<MovementComponent> mm;
   private ComponentMapper<IsPlayerComponent> im;
   Controller p1, p2, p3, p4;

   public PlayerControlSystem() {
      super(Utilities.MAX_STEP_TIME);
      mm = ComponentMapper.getFor(MovementComponent.class);
      im = ComponentMapper.getFor(IsPlayerComponent.class);
      if (Controllers.getControllers().size >= 1)
         p1 = Controllers.getControllers().get(0);
      if (Controllers.getControllers().size >= 2)
         p2 = Controllers.getControllers().get(1);
      if (Controllers.getControllers().size >= 3)
         p3 = Controllers.getControllers().get(2);
      if (Controllers.getControllers().size >= 4)
         p4 = Controllers.getControllers().get(3);
   }

   @Override
   public void addedToEngine(Engine engine) {
      entities = engine.getEntitiesFor(Family.all(IsPlayerComponent.class,MovementComponent.class).get());
   }

   @Override
   protected void updateInterval() {
      for (Entity entity : entities) {
         MovementComponent mc = mm.get(entity);
         IsPlayerComponent ic = im.get(entity);
         if(ic.playerNum == 0) {
            mc.moveX = Gdx.input.isKeyPressed(Input.Keys.A) ? -1
                        : Gdx.input.isKeyPressed(Input.Keys.D) ||
                          Gdx.input.isKeyPressed(Input.Keys.E) ? 1 : 0;
            mc.moveX = Gdx.input.isKeyPressed(Input.Keys.A) &&
                        (Gdx.input.isKeyPressed(Input.Keys.E) ||
                         Gdx.input.isKeyPressed(Input.Keys.D)) ? 0 : mc.moveX;
            mc.moveY = Gdx.input.isKeyPressed(Input.Keys.W) ||
                       Gdx.input.isKeyPressed(Input.Keys.COMMA) ? 1
                        : Gdx.input.isKeyPressed(Input.Keys.S) ||
                          Gdx.input.isKeyPressed(Input.Keys.O) ? -1 : 0;
            mc.moveY = (Gdx.input.isKeyPressed(Input.Keys.W) ||
                        Gdx.input.isKeyPressed(Input.Keys.COMMA)) &&
                       (Gdx.input.isKeyPressed(Input.Keys.S) ||
                        Gdx.input.isKeyPressed(Input.Keys.O)) ? 0 : mc.moveY;
         }
         if (p1 != null) {
            mc.moveX = p1.getAxis(1);
            mc.moveY = -p1.getAxis(0);
         }
         if(ic.playerNum == 1) {
            mc.moveX = p2.getAxis(1);
            mc.moveY = -p2.getAxis(0);
            mc.shootX = p2.getAxis(3);
            mc.shootY = -p2.getAxis(2);
            mc.shot = (Math.abs(mc.shootX) > 0.2 || Math.abs(mc.shootY) > 0.2);
         }
         if(ic.playerNum == 2) {
            if (p3 != null) {
               mc.moveX = p3.getAxis(1);
               mc.moveY = -p3.getAxis(0);
               mc.shootX = p3.getAxis(3);
               mc.shootY = -p3.getAxis(2);
               mc.shot = (Math.abs(mc.shootX) > 0.2 || Math.abs(mc.shootY) > 0.2);
            }
         }

         if(ic.playerNum == 3) {
            if (p4 != null) {
               mc.moveX = p4.getAxis(1);
               mc.moveY = -p4.getAxis(0);
               mc.shootX = p4.getAxis(3);
               mc.shootY = -p4.getAxis(2);
               mc.shot = (Math.abs(mc.shootX) > 0.2 || Math.abs(mc.shootY) > 0.2);
            }
         }
      }
   }
}
