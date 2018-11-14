package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;
import com.mygdx.game.components.SteeringComponent;
import com.mygdx.game.utilities.SteeringPresets;

/**
 * A system responsible for AI.
 */
public class SteeringSystem extends IteratingSystem {

   private ComponentMapper<SteeringComponent> steeringComponentComponentMapper;

   public SteeringSystem(){
      super(Family.all(SteeringComponent.class).get());
      steeringComponentComponentMapper=ComponentMapper.getFor(SteeringComponent.class);
   }

   @Override
   public void update(float deltaTime){
      super.update(deltaTime);
      GdxAI.getTimepiece().update(deltaTime);

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
         SteeringComponent steeringComponent=steeringComponentComponentMapper.get(entity);
         try{
            steeringComponent.update(deltaTime);
         }catch (NullPointerException e){
            steeringComponent.steeringBehavior= SteeringPresets.getWander(steeringComponent);
         }
   }
}
