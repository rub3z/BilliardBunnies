package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.utilities.Tile;

/**
 * This component stores variables related to an enemy.
 */
public class EnemyStatsComponent implements Component, Pool.Poolable {

   public float rof = 1f;
   public boolean shoot=false;
   public float speed=20f;
   public int health;
   public Entity target;
   public boolean aimedAtTarget=false;
   public int bulletType=0;
   public int otherDirection;
   public Tile currentTile;
   public Tile previousTile;
   public float timer;
   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {
      rof=1f;
      shoot=false;
      speed=20f;
      target=null;
      aimedAtTarget=false;
      bulletType=0;
      otherDirection = 0;
      currentTile = null;
      previousTile = null;
   }
}
