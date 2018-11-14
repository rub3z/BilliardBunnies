package com.mygdx.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.components.IsPlayerComponent;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utilities.Utilities;

/**
 * A system that detect an end of the game.
 */
public class DetectEndGameSystem extends IntervalSystem {

   ImmutableArray<Entity> entities;
   public DetectEndGameSystem(){
      super(Utilities.MAX_STEP_TIME);
   }

   @Override
   public void addedToEngine(Engine engine) {
      super.addedToEngine(engine);
      entities=engine.getEntitiesFor(Family.all(IsPlayerComponent.class).get());
   }

   /**
    * The processing logic of the system should be placed here.
    */
   @Override
   protected void updateInterval() {
      if(entities.size()==0){
         GameScreen.getGameScreen().endGame();
      }
   }
}
