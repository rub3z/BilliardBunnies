package com.mygdx.game.utilities;

import com.badlogic.gdx.utils.IntMap;

/**
 * A generic representation of a tile
 */
public class Tile {
   private float x;
   private float y;
   private LevelManager.Type type;
   public IntMap<Tile> neighbors;

   /**
    * Construct a new tile.
    *
    * @param x    float coordinate.
    * @param y    float coordinate
    * @param type type of the tile.
    */
   public Tile(float x, float y, LevelManager.Type type) {
      this.x = x;
      this.y = y;
      this.type = type;
      neighbors=new IntMap<Tile>();
   }

   /**
    * Get x float coordinate
    *
    * @return x
    */
   public float getX() {
      return x;
   }

   /**
    * Set x float coordinate.
    *
    * @param x
    */
   public void setX(float x) {
      this.x = x;
   }

   /**
    * Get y float coordinate.
    *
    * @return y
    */
   public float getY() {
      return y;
   }

   /**
    * Set y float coordinate.
    *
    * @param y
    */
   public void setY(float y) {
      this.y = y;
   }

   /**
    * Get tile's type.
    *
    * @return type.
    */
   public LevelManager.Type getType() {
      return type;
   }

   /**
    * Set tile type.
    *
    * @param type
    */
   public void setType(LevelManager.Type type) {
      this.type = type;
   }


   public static final int LEFT = 1;
   public static final int RIGHT = 2;
   public static final int UP = 3;
   public static final int DOWN = 4;
}
