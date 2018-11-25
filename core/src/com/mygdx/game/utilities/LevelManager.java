package com.mygdx.game.utilities;

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.utilities.maze.MazeGenerator;

/**
 * Manage level information.
 */
public class LevelManager {
   private static LevelManager single_instance; //Instance of this class.
   private int[][] mazeData; //Data generated from the maze generator.
   private MazeGenerator mazeGenerator; //Maze generator.
   private float tileScale; //How big is the tile size is.
   private Array<Tile> tiles; //Tiles container.
   private boolean isCenter;

   /**
    * Private constructor.
    */
   private LevelManager() {
      mazeGenerator = new MazeGenerator();
      tiles = new Array<Tile>();
   }

   /**
    * Get a single instance of this class.
    *
    * @return LevelManager.
    */
   public static LevelManager getManager() {
      if (single_instance == null) {
         single_instance = new LevelManager();
      }
      return single_instance;
   }

   /**
    * Generate a new level.
    *
    * @param columnNumber number of column of cells
    * @param rowNumber    number of row of cells
    * @param cellWidth    the number of tile equals to the width the cell. A minimum width of cell is 3 tiles.
    * @param cellHeight   the number of tile equals to the height of the cell. A minimum height of cell is 3 tiles.
    * @param tileScale    equivalence of 1 integer index to float value
    * @param isCenter     should the maze be center on the screen.
    */
   public void generateLevel(int columnNumber, int rowNumber, int cellWidth, int cellHeight, float tileScale, boolean isCenter) {
      mazeData = mazeGenerator.generateMaze(rowNumber, columnNumber, cellWidth, cellHeight);
      this.tileScale = tileScale;
      this.isCenter = isCenter;
      for (int y = 0; y < mazeData.length; y++) {
         for (int x = 0; x < mazeData[0].length; x++) {
            tiles.add(new Tile((x * tileScale) + getHorizontalShift(), (y * tileScale) + getVerticalShift(), (mazeData[y][x] == 0) ? Type.EMPTY_SPACE : Type.WALL));
         }
      }
   }

   /**
    * Generate a new level with a cell size of 5x5 tiles and try to center around the screen.
    *
    * @param columnNumber number of column of cells
    * @param rowNumber    number of row of cells
    * @param tileScale    equivalence of 1 integer index to float value
    */
   public void generateLevel(int columnNumber, int rowNumber, float tileScale) {
      generateLevel(columnNumber, rowNumber, 7, 7, tileScale, true);
   }

   /**
    * Return immutable array of tiles.
    *
    * @return
    */
   public ImmutableArray<Tile> getTiles() {
      return new ImmutableArray<Tile>(tiles);
   }

   /**
    * Return a specific tiles by indexes.
    *
    * @param x int index.
    * @param y int index.
    * @return a tile located at the above index.
    */
   public Tile getTile(int x, int y) {
      return tiles.get((y * mazeData.length) + x);
   }

   /**
    * Return a specific tiles by floating point coordinate.
    *
    * @param x float x-coordinate.
    * @param y float y-coordinate.
    * @return a tile located at the above location.
    */
   public Tile getTile(float x, float y) {
      x=x-getHorizontalShift();
      y=y-getVerticalShift();
      return tiles.get(((int) (y / tileScale) * mazeData.length) + (int) (x / tileScale));
   }

   /**
    * Convert integer indexes to float coordinates.
    *
    * @param x int index.
    * @param y int index.
    * @return Vector2 contains float coordinates.
    */
   public Vector2 intToFloat(int x, int y) {
      return new Vector2((x * tileScale), (y * tileScale));
   }

   /**
    * Get all tiles that is classified as a wall.
    *
    * @return all wall tiles.
    */
   public ImmutableArray<Tile> getWallTitles() {
      Array<Tile> temp = new Array<Tile>();
      for (Tile t : tiles) {
         if (t.getType() == Type.WALL) {
            temp.add(t);
         }
      }

      return new ImmutableArray<Tile>(temp);
   }

   /**
    * Get all tiles that is classified as empty space.
    *
    * @return all empty space tiles.
    */
   public ImmutableArray<Tile> getEmptyTiles() {
      Array<Tile> temp = new Array<Tile>();
      for (Tile t : tiles) {
         if (t.getType() == Type.EMPTY_SPACE) {
            temp.add(t);
         }
      }

      return new ImmutableArray<Tile>(temp);
   }

   /**
    * Get a random tile that is classified as a wall.
    *
    * @return a random wall tile.
    */
   public Tile getARandomWallTile() {
      Tile t;
      do {
         t = getTile(MathUtils.random(mazeData[0].length), MathUtils.random(mazeData.length));
      } while (t.getType() != Type.WALL);
      return t;
   }

   /**
    * Get a random tile that is classified as an empty space.
    *
    * @return a random empty space tile.
    */
   public Tile getARandomEmptySpaceTile() {
      Tile t;
      do {
         t = getTile(MathUtils.random(mazeData[0].length), MathUtils.random(mazeData.length));
      } while (t.getType() != Type.EMPTY_SPACE);
      return t;
   }

   /**
    * Type of tiles.
    */
   public enum Type {
      WALL, EMPTY_SPACE
   }

   public float getHorizontalShift() {

      System.out.println( Utilities.FRUSTUM_WIDTH );
      return isCenter ? (Utilities.FRUSTUM_WIDTH - ((mazeData[0].length) * tileScale)) / 2f: 0f;
   }

   public float getVerticalShift() {
      return isCenter ? (Utilities.FRUSTUM_HEIGHT - ((mazeData.length) * tileScale)) / 2f : 0;
   }

}
