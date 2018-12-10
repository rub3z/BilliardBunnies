package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.components.BodyComponent;
import com.mygdx.game.components.IsRabbitComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.entities.Factory;
import com.mygdx.game.utilities.Utilities;

public class RabbitSpriteSwitcher extends IteratingSystem {
   private ComponentMapper<IsRabbitComponent> irc;
   private ComponentMapper<BodyComponent> bc;
   private ComponentMapper<TextureComponent> tc;

   /**
    * Instantiates a system that will iterate over the entities described by the Family.
    */
   public RabbitSpriteSwitcher() {
      super(Family.all(IsRabbitComponent.class, BodyComponent.class, TextureComponent.class).get());
      irc = ComponentMapper.getFor(IsRabbitComponent.class);
      bc = ComponentMapper.getFor(BodyComponent.class);
      tc = ComponentMapper.getFor(TextureComponent.class);
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
      int direction = -1;
      float angle = Utilities.vectorToAngle(bc.get(entity).body.getLinearVelocity());
      if (angle < 0) {
         angle += MathUtils.PI * 2;
      }
      if (!bc.get(entity).body.getLinearVelocity().isZero(0.001f)) {
         tc.get(entity).isPlay = true;
         if (angle >= 0.785398f && angle <= 2.35619f) {
            direction = 2;
         } else if (angle > 2.35619f && angle < 3.92699f) {
            direction = 0;
         } else if (angle >= 3.92699f && angle < 5.49779f) {
            direction = 3;
         } else {
            direction = 1;
         }
         if (direction != irc.get(entity).direction) {
            switch (direction) {
               case 0:
                  tc.get(entity).reset();
                  tc.get(entity).textureRegionAnimation = Factory.getFactory().createTexture("Bunny_0", 10f);
                  tc.get(entity).name = "Bunny_0";
                  irc.get(entity).direction = direction;
                  break;
               case 1:
                  tc.get(entity).reset();
                  tc.get(entity).textureRegionAnimation = Factory.getFactory().createTexture("Bunny_1", 10f);
                  tc.get(entity).name = "Bunny_1";
                  irc.get(entity).direction = direction;
                  break;
               case 2:
                  tc.get(entity).reset();
                  tc.get(entity).textureRegionAnimation = Factory.getFactory().createTexture("Bunny_3", 10f);
                  tc.get(entity).name = "Bunny_3";
                  irc.get(entity).direction = direction;
                  break;
               case 3:
                  tc.get(entity).reset();
                  tc.get(entity).textureRegionAnimation = Factory.getFactory().createTexture("Bunny_2", 10f);
                  tc.get(entity).name = "Bunny_2";
                  irc.get(entity).direction = direction;
                  break;
               default:
                  break;
            }
         }

      } else {
         tc.get(entity).isPlay = false;
      }
   }
}
