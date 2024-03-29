package com.mygdx.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.components.IsHeroComponent;
import com.mygdx.game.components.IsPlayerComponent;
import com.mygdx.game.components.IsSeedComponent;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utilities.Utilities;

/**
 * A system that detect an end of the game.
 */
public class DetectEndGameSystem extends IntervalSystem {

   ImmutableArray<Entity> entities;
   ImmutableArray<Entity> seeds;
   public DetectEndGameSystem(){
      super(Utilities.MAX_STEP_TIME);
   }

   @Override
   public void addedToEngine(Engine engine) {
      super.addedToEngine(engine);
      entities=engine.getEntitiesFor(Family.all(IsHeroComponent.class).get());
      seeds = engine.getEntitiesFor(Family.all(IsSeedComponent.class).get());
   }

   /**
    * The processing logic of the system should be placed here.
    */
   @Override
   protected void updateInterval() {
      if(entities.get(0).getComponent(IsPlayerComponent.class).health <= 0 ){
         GameScreen.getGameScreen().endGame();
      }
      if (seeds.size() == 0) {
         GameScreen.getGameScreen().endGame();
      }
   }
}
