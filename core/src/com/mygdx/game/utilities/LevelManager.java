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
   private int[][] mazeData; //Data generated from the maze generator.
   private MazeGenerator mazeGenerator; //Maze generator.
   private float tileSize; //How big is the tile size is.
   private Array<Tile> tiles; //Tiles container.
   private static LevelManager single_instance; //Instance of this class.

   /**
    * Get a single instance of this class.
    *
    * @return LevelManager.
    */
   public LevelManager getManager() {
      if (single_instance == null) {
         single_instance = new LevelManager();
      }
      return single_instance;
   }

   /**
    * Private constructor.
    */
   private LevelManager() {
      mazeGenerator = new MazeGenerator();
      tiles = new Array<Tile>();
   }

   /**
    * Generate a new level.
    *
    * @param columnNumber number of column of cells
    * @param rowNumber    number of row of cells
    * @param scale        size of the cell. A minimum size of cell is 3x3 tiles.
    * @param width        width of the level.
    * @param height       height of the level.
    */
   public void generateLevel(int columnNumber, int rowNumber, int scale, float width, float height) {
      mazeData = mazeGenerator.generateMaze(rowNumber, columnNumber, scale);
      tileSize = ((width / mazeData[0].length) < (height / mazeData.length)) ? (width / mazeData[0].length) : (height / mazeData.length);
      for (int y = 0; y < mazeData.length; y++) {
         for (int x = 0; x < mazeData[0].length; x++) {
            tiles.add(new Tile(x * tileSize, y * tileSize, (mazeData[y][x] == 0) ? Type.EMPTY_SPACE : Type.WALL));
         }
      }
   }

   /**
    * Generate a new level with a cell size of 5x5 tiles.
    *
    * @param columnNumber number of column of cells
    * @param rowNumber    number of row of cells
    * @param width        width of the level.
    * @param height       height of the level.
    */
   public void generateLevel(int columnNumber, int rowNumber, float width, float height) {
      generateLevel(columnNumber, rowNumber, 5, width, height);
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
    * Return a specific tiles.
    *
    * @param x int index.
    * @param y int index.
    * @return a tile located at the above index.
    */
   public Tile getTile(int x, int y) {
      return tiles.get((y * mazeData.length) + x);
   }

   /**
    * Return a specific tiles.
    *
    * @param x float x-coordinate.
    * @param y float y-coordinate.
    * @return a tile located at the above location.
    */
   public Tile getTile(float x, float y) {
      return tiles.get(((int) (y / tileSize) * mazeData.length) + (int) (x / tileSize));
   }

   /**
    * Convert integer indexes to float coordinates.
    *
    * @param x int index.
    * @param y int index.
    * @return Vector2 contains float coordinates.
    */
   public Vector2 intToFloat(int x, int y) {
      return new Vector2(x * tileSize, y * tileSize);
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

}
